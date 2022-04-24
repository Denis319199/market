package com.db.utility.filter;

import com.db.utility.sql.SqlQueryBuilder;
import com.db.utility.Utilities;
import com.db.utility.filter.annotation.FilterInnerJoin;
import com.db.utility.filter.annotation.FilterModel;
import com.db.utility.filter.annotation.FilterOperation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Filter {

  public static void doFilter(Object filterObj) {
    try {
      List<String> whereConditionList = new ArrayList<>();
      Class<?> filterClass = filterObj.getClass();
      Class<?> modelClass = filterClass.getAnnotation(FilterModel.class).value();
      String tableName = modelClass.getAnnotation(Table.class).name();

      Integer page = null;
      Integer size = null;
      String[] orderBy = null;
      Boolean[] ascOrder = null;

      SqlQueryBuilder sqlQueryBuilder =
          SqlQueryBuilder.builder().select(tableName + ".*").from(tableName);

      for (Field filterField : filterClass.getDeclaredFields()) {
        String whereCondition = getWhereCondition(filterObj, filterClass, filterField, modelClass);
        if (whereCondition != null) {
          whereConditionList.add(whereCondition);
          continue;
        }

        FilterInnerJoin filterInnerJoin = filterField.getAnnotation(FilterInnerJoin.class);
        if (filterInnerJoin != null) {
          Object innerFilterObject =
              filterClass.getDeclaredMethod(getGetterName(filterField.getName())).invoke(filterObj);
          doInnerFilter(
              innerFilterObject, whereConditionList, sqlQueryBuilder, filterInnerJoin, modelClass);
          continue;
        }

        switch (filterField.getName()) {
          case "page":
            page = (Integer) filterClass.getDeclaredMethod("getPage").invoke(filterObj);
            break;
          case "size":
            size = (Integer) filterClass.getDeclaredMethod("getSize").invoke(filterObj);
            break;
          case "orderBy":
            if (filterField.getType().isArray()) {
              orderBy = (String[]) filterClass.getDeclaredMethod("getOrderBy").invoke(filterObj);
            } else {
              orderBy =
                  new String[] {
                    (String) filterClass.getDeclaredMethod("getOrderBy").invoke(filterObj)
                  };
            }

            break;
          case "ascOrder":
            if (filterField.getType().isArray()) {
              ascOrder = (Boolean[]) filterClass.getDeclaredMethod("getAscOrder").invoke(filterObj);
            } else {
              ascOrder =
                  new Boolean[] {
                    (Boolean) filterClass.getDeclaredMethod("getAscOrder").invoke(filterObj)
                  };
            }
            break;
        }
      }

      sqlQueryBuilder.where(String.join(" AND ", whereConditionList.toArray(new String[0])));

      if (orderBy != null && ascOrder != null) {
        if (orderBy.length != ascOrder.length) {
          throw new Exception("Different orderBy and ascOrder length");
        }

        for (int i = 0; i < orderBy.length; ++i) {
          orderBy[i] = getFullColumnName(orderBy[i], modelClass);
        }

        sqlQueryBuilder.orderBy(orderBy, ascOrder);
      }

      if (size != null) {
        sqlQueryBuilder.limit(size);

        if (page != null) {
          sqlQueryBuilder.offset(size * page);
        }
      }

      String result = sqlQueryBuilder.build();

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  private static void doInnerFilter(
      Object filterObj,
      List<String> whereConditionList,
      SqlQueryBuilder sqlQueryBuilder,
      FilterInnerJoin filterInnerJoin,
      Class<?> lhsModelClass) {
    try {
      Class<?> filterClass = filterObj.getClass();
      Class<?> modelClass = filterClass.getAnnotation(FilterModel.class).value();
      String tableName = modelClass.getAnnotation(Table.class).name();

      String[] lhs = filterInnerJoin.lhs();
      String[] rhs = filterInnerJoin.rhs();

      if (lhs.length != rhs.length) {
        throw new Exception("Inner join's left hand size doesn't have right one or vice versa");
      } else if (lhs.length == 0) {
        throw new Exception("There is no criteria to join tables");
      }

      sqlQueryBuilder
          .innerJoin(tableName)
          .on(getFullColumnName(lhs[0], lhsModelClass))
          .append(" = ")
          .append(getFullColumnName(rhs[0], modelClass));

      for (int i = 1; i < lhs.length; ++i) {
        sqlQueryBuilder
            .and(getFullColumnName(lhs[0], lhsModelClass))
            .append(" = ")
            .append(getFullColumnName(rhs[i], modelClass));
      }

      for (Field filterField : filterClass.getDeclaredFields()) {
        String whereCondition = getWhereCondition(filterObj, filterClass, filterField, modelClass);

        if (whereCondition != null) {
          whereConditionList.add(whereCondition);
        } else {
          FilterInnerJoin filterInnerInnerJoin = filterField.getAnnotation(FilterInnerJoin.class);

          if (filterInnerInnerJoin != null) {
            Object innerFilterObject =
                filterClass
                    .getDeclaredMethod(getGetterName(filterField.getName()))
                    .invoke(filterObj);
            doInnerFilter(
                innerFilterObject,
                whereConditionList,
                sqlQueryBuilder,
                filterInnerInnerJoin,
                modelClass);
          }
        }
      }

    } catch (Exception ex) {

    }
  }

  private static String getFullColumnName(String fieldName, Class<?> modelClass) throws Exception {
    try {
      Field modelField = modelClass.getDeclaredField(fieldName);

      Column columnAnnotation = modelField.getAnnotation(Column.class);
      String columnName =
          columnAnnotation == null
              ? Utilities.translateFromCamelToSnakeCase(modelField.getName())
              : columnAnnotation.name();

      return modelClass.getAnnotation(Table.class).name() + '.' + columnName;
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  private static String getWhereCondition(
      Object filterObj, Class<?> filterClass, Field filterField, Class<?> modelClass)
      throws Exception {
    try {
      FilterOperation filterOperation = filterField.getAnnotation(FilterOperation.class);

      if (filterOperation != null) {

        String filterFieldName = filterField.getName();

        String fullColumnName =
            getFullColumnName(
                filterOperation.fieldName().isEmpty()
                    ? filterFieldName
                    : filterOperation.fieldName(),
                modelClass);

        StringBuilder whereCondition = new StringBuilder(fullColumnName + ' ');

        if (filterOperation.flag().isEmpty()) {
          whereCondition.append(filterOperation.op().getSign());
        } else {
          Field flagField = filterClass.getDeclaredField(filterOperation.flag());
          String flagGetter = getGetterName(flagField.getName());

          if (!flagField.getType().equals(Boolean.class)) {
            throw new Exception("Operation flag is not boolean type");
          }

          if ((boolean) filterClass.getMethod(flagGetter).invoke(filterObj)) {
            whereCondition.append(filterOperation.op().getSign());
          } else {
            whereCondition.append(filterOperation.op().getOpposite().getSign());
          }
        }

        String filterFieldValue =
                filterClass
                        .getDeclaredMethod(getGetterName(filterFieldName))
                        .invoke(filterObj)
                        .toString();

        Class<?> filterFieldClass = filterField.getType();
        if (filterFieldClass.equals(String.class) || filterFieldClass.equals(LocalDate.class)) {
          whereCondition.append(" '").append(filterFieldValue).append("'");
        } else {
          whereCondition.append(' ').append(filterFieldValue);
        }

        return whereCondition.toString();
      }
      return null;
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public static String getGetterName(String fieldName) {
    StringBuilder getterName = new StringBuilder("get");
    getterName.append(fieldName);
    getterName.setCharAt(3, Character.toUpperCase(fieldName.charAt(0)));
    return getterName.toString();
  }
}
