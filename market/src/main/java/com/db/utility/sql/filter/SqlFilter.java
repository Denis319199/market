package com.db.utility.sql.filter;

import com.db.exception.ServiceException;
import com.db.utility.sql.filter.model.FilterCountResult;
import com.db.utility.sql.filter.model.FilterResult;
import com.db.utility.sql.SqlQueryBuilder;
import com.db.utility.Utilities;
import com.db.utility.sql.filter.annotation.FilterInnerJoin;
import com.db.utility.sql.filter.annotation.FilterModel;
import com.db.utility.sql.filter.annotation.FilterOperation;
import com.db.utility.sql.SqlQueryExecutor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SqlFilter {

  private final SqlQueryExecutor sqlQueryExecutor;

  private static class InnerFilter {
    private final Object filterObj;
    private final Class<?> filterClass;
    @Getter private final Class<?> modelClass;
    private final String tableName;
    private FilterInnerJoin innerJoin;
    private final List<InnerFilter> innerFilters = new ArrayList<>();

    private final StringJoiner condition = new StringJoiner(" AND ");

    private Integer page;
    @Getter private Integer size;
    private String[] orderBy;
    private Boolean[] ascOrder;

    public InnerFilter(Object filterObj) throws SqlFilterException {
      this.filterObj = filterObj;
      this.filterClass = filterObj.getClass();
      this.modelClass = filterClass.getAnnotation(FilterModel.class).value();
      this.tableName = modelClass.getAnnotation(Table.class).name();
      parseFilter();
    }

    private InnerFilter(Object filterObj, Class<?> filterClass, FilterInnerJoin filterInnerJoin)
        throws SqlFilterException {
      this.filterObj = filterObj;
      this.filterClass = filterClass;
      this.modelClass = filterClass.getAnnotation(FilterModel.class).value();
      this.tableName = modelClass.getAnnotation(Table.class).name();
      this.innerJoin = filterInnerJoin;

      if (filterObj != null) {
        parseInnerJoinFilter();
      } else {
        parseEmptyInnerJoinFilter();
      }
    }

    public String buildQuery() throws SqlFilterException {
      SqlQueryBuilder sqlQueryBuilder = SqlQueryBuilder.builder().select("*");

      buildFromForQuery(sqlQueryBuilder);

      buildWhereForQuery(sqlQueryBuilder);

      buildOrderByForQuery(sqlQueryBuilder);

      buildLimitAndOffsetForQuery(sqlQueryBuilder);

      return sqlQueryBuilder.build();
    }

    public String buildCountQuery() throws SqlFilterException {
      SqlQueryBuilder sqlQueryBuilder = SqlQueryBuilder.builder().select("COUNT(*)");

      buildFromForQuery(sqlQueryBuilder);

      buildWhereForQuery(sqlQueryBuilder);

      return sqlQueryBuilder.build();
    }

    private void buildFromForQuery(SqlQueryBuilder sqlQueryBuilder) throws SqlFilterException {
      if (innerFilters.isEmpty()) {
        sqlQueryBuilder.from(tableName);
      } else {
        sqlQueryBuilder.from("");
        buildFromForQueryImpl(sqlQueryBuilder);
      }
    }

    private void buildFromForQueryImpl(SqlQueryBuilder sqlQueryBuilder) throws SqlFilterException {
      for (InnerFilter innerFilter : innerFilters) {
        FilterInnerJoin filterInnerJoin = innerFilter.innerJoin;
        String[] lhs = filterInnerJoin.lhs();
        String[] rhs = filterInnerJoin.rhs();

        if (lhs.length != rhs.length) {
          throw new SqlFilterException(
              SqlFilterException.INVALID_CONDITION_FOR_INNER_JOIN,
              "Inner join's left hand size doesn't have right one or vice versa");
        } else if (lhs.length == 0) {
          throw new SqlFilterException(
              SqlFilterException.INVALID_CONDITION_FOR_INNER_JOIN,
              "There is no criteria to join tables");
        }

        StringJoiner joinCondition = new StringJoiner(" AND ");
        for (int i = 0; i < lhs.length; ++i) {
          joinCondition.add(
              getFullColumnName(lhs[0]) + " = " + innerFilter.getFullColumnName(rhs[i]));
        }

        sqlQueryBuilder
            .append(tableName)
            .innerJoin(innerFilter.tableName)
            .on(joinCondition.toString());

        innerFilter.buildFromForQueryImpl(sqlQueryBuilder);
      }
    }

    private void buildWhereForQuery(SqlQueryBuilder sqlQueryBuilder) {
      for (InnerFilter innerFilter : innerFilters) {
        mergeConditions(innerFilter);
      }
      if (condition.length() != 0) {
        sqlQueryBuilder.where(condition.toString());
      }
    }

    private void mergeConditions(InnerFilter filter) {
      if (filter.condition.length() != 0) {
        condition.add(filter.condition.toString());
      }

      for (InnerFilter innerFilter : filter.innerFilters) {
        mergeConditions(innerFilter);
      }
    }

    private void buildOrderByForQuery(SqlQueryBuilder sqlQueryBuilder) throws SqlFilterException {
      if (ascOrder != null) {
        if (orderBy != null) {
          if (orderBy.length != ascOrder.length) {
            throw new SqlFilterException(SqlFilterException.DIFFERENT_SORTING_PARAMETER_LENGTHS);
          }

          sqlQueryBuilder.orderBy(orderBy, ascOrder);
        }
      } else if (orderBy != null) {
        sqlQueryBuilder.orderBy(orderBy);
      } else {
        String idFieldName =
            Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) != null)
                .map(Field::getName)
                .findFirst()
                .orElseThrow(() -> new SqlFilterException(SqlFilterException.NO_ID_FOR_ORDER));

        sqlQueryBuilder.orderBy(new String[] {getFullColumnName(idFieldName)});
      }
    }

    private void buildLimitAndOffsetForQuery(SqlQueryBuilder sqlQueryBuilder)
        throws SqlFilterException {
      if (size != null && page != null) {
        if (size > 0 && page >= 0) {
          sqlQueryBuilder.limit(size);
          sqlQueryBuilder.offset(size * page);
        } else {
          throw new SqlFilterException(SqlFilterException.INVALID_PAGE_NUMBER_OR_PAGE_SIZE);
        }
      }
    }

    private void parseInnerJoinFilter() throws SqlFilterException {
      for (Field filterField : filterClass.getDeclaredFields()) {
        FilterOperation filterOperation = filterField.getAnnotation(FilterOperation.class);
        if (filterOperation != null) {
          addCondition(filterField, filterOperation);
          continue;
        }

        FilterInnerJoin filterInnerJoin = filterField.getAnnotation(FilterInnerJoin.class);
        if (filterInnerJoin != null) {
          innerFilters.add(
              new InnerFilter(
                  getFieldValue(filterField.getName()), filterField.getType(), filterInnerJoin));
        }
      }
    }

    private void parseEmptyInnerJoinFilter() throws SqlFilterException {
      for (Field filterField : filterClass.getDeclaredFields()) {
        FilterInnerJoin filterInnerJoin = filterField.getAnnotation(FilterInnerJoin.class);
        if (filterInnerJoin != null) {
          innerFilters.add(
              new InnerFilter(
                  getFieldValue(filterField.getName()), filterField.getType(), filterInnerJoin));
        }
      }
    }

    private void parseFilter() throws SqlFilterException {
      for (Field filterField : filterClass.getDeclaredFields()) {
        FilterOperation filterOperation = filterField.getAnnotation(FilterOperation.class);
        if (filterOperation != null) {
          addCondition(filterField, filterOperation);
          continue;
        }

        FilterInnerJoin filterInnerJoin = filterField.getAnnotation(FilterInnerJoin.class);
        if (filterInnerJoin != null) {
          innerFilters.add(
              new InnerFilter(
                  getFieldValue(filterField.getName()), filterField.getType(), filterInnerJoin));
          continue;
        }

        if (innerJoin == null) {
          checkForSpecFields(filterField);
        }
      }
    }

    private void checkForSpecFields(Field filterField) throws SqlFilterException {
      try {
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
              if (orderBy != null) {
                for (int i = 0; i < orderBy.length; ++i) {
                  orderBy[i] = getFullColumnName(orderBy[i]);
                }
              }
            } else {
              String orderByValue =
                  (String) filterClass.getDeclaredMethod("getOrderBy").invoke(filterObj);
              if (orderByValue != null) {
                orderBy = new String[] {getFullColumnName(orderByValue)};
              }
            }
            break;

          case "ascOrder":
            if (filterField.getType().isArray()) {
              ascOrder = (Boolean[]) filterClass.getDeclaredMethod("getAscOrder").invoke(filterObj);
            } else {
              Boolean ascOrderValue =
                  (Boolean) filterClass.getDeclaredMethod("getAscOrder").invoke(filterObj);
              if (ascOrderValue != null) {
                ascOrder = new Boolean[] {ascOrderValue};
              }
            }
            break;
        }
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignore) {
        throw new SqlFilterException(SqlFilterException.CANNOT_GET_ACCESS_TO_FIELD);
      }
    }

    private Object getFieldValue(String fieldName) throws SqlFilterException {
      try {
        StringBuilder getterName = new StringBuilder("get");
        getterName.append(fieldName);
        getterName.setCharAt(3, Character.toUpperCase(fieldName.charAt(0)));

        return filterClass.getDeclaredMethod(getterName.toString()).invoke(filterObj);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
        throw new SqlFilterException(
            SqlFilterException.CANNOT_GET_ACCESS_TO_FIELD, "Field name: '" + fieldName + '\'');
      }
    }

    private void addCondition(Field filterField, FilterOperation filterOperation)
        throws SqlFilterException {
      String filterFieldName = filterField.getName();

      String fullColumnName =
          getFullColumnName(
              filterOperation.fieldName().isEmpty()
                  ? filterFieldName
                  : filterOperation.fieldName());

      String sign = getSign(filterOperation);

      try {
        if (filterField.getType().isArray()) {
          StringJoiner conditionWithOrDelim = new StringJoiner(" OR ", "( ", " )");

          Object[] condValues = (Object[]) getFieldValue(filterFieldName);

          if (condValues != null) {
            for (Object condValue : condValues) {
              conditionWithOrDelim.add(
                  fullColumnName + ' ' + sign + ' ' + convertValueToString(condValue));
            }

            condition.add(conditionWithOrDelim.toString());
          }
        } else {
          Object condValue = getFieldValue(filterFieldName);

          String value = convertValueToString(condValue);
          if (value != null) {
            condition.add(fullColumnName + ' ' + sign + ' ' + value);
          }
        }

      } catch (Exception ignore) {
      }
    }

    private String getSign(FilterOperation filterOperation) {
      try {
        if (!filterOperation.flag().isEmpty()) {
          Field flagField = filterClass.getDeclaredField(filterOperation.flag());

          if (!flagField.getType().equals(Boolean.class)) {
            throw new Exception("Operation flag is not boolean type");
          }

          Object flagValue = getFieldValue(flagField.getName());
          if (flagValue != null && !(Boolean) flagValue) {
            return filterOperation.op().getOpposite().getSign();
          }
        }
        return filterOperation.op().getSign();
      } catch (Exception ignore) {
        return null;
      }
    }

    private String convertValueToString(Object condValue) {
      Class<?> filterFieldClass = condValue.getClass();
      if (filterFieldClass.equals(String.class) || filterFieldClass.equals(LocalDate.class)) {
        return '\'' + condValue.toString() + '\'';
      } else {
        return condValue.toString();
      }
    }

    private String getFullColumnName(String fieldName) throws SqlFilterException {
      try {
        Field modelField = modelClass.getDeclaredField(fieldName);

        Column columnAnnotation = modelField.getAnnotation(Column.class);
        String columnName =
            columnAnnotation == null
                ? Utilities.translateFromCamelToSnakeCase(modelField.getName())
                : columnAnnotation.name();

        return modelClass.getAnnotation(Table.class).name() + '.' + columnName;
      } catch (NoSuchFieldException ex) {
        throw new SqlFilterException(
            SqlFilterException.THERE_IS_NO_SUCH_ATTRIBUTE, "Attribute name: '" + fieldName + '\'');
      }
    }
  }

  public <T> FilterResult<T> doFilter(Object filter, Class<T> model) throws ServiceException {
    try {
      InnerFilter innerFilter = new InnerFilter(filter);

      FilterResult<T> result = new FilterResult<>();

      Integer totalElementCount =
          sqlQueryExecutor
              .execute(innerFilter.buildCountQuery(), FilterCountResult.class)
              .get(0)
              .getCount();
      Integer pageSize = innerFilter.getSize();

      result.setTotalElementCount(totalElementCount);
      result.setTotalPageCount(
          totalElementCount % pageSize == 0
              ? totalElementCount / pageSize
              : totalElementCount / pageSize + 1);
      result.setContent(sqlQueryExecutor.execute(innerFilter.buildQuery(), model));
      return result;
    } catch (SqlFilterException ex) {
      switch (ex.getMessage()) {
        case SqlFilterException.THERE_IS_NO_SUCH_ATTRIBUTE:
        case SqlFilterException.DIFFERENT_SORTING_PARAMETER_LENGTHS:
        case SqlFilterException.INVALID_PAGE_NUMBER_OR_PAGE_SIZE:
          throw new ServiceException(ex.getFullMessage(), HttpStatus.BAD_REQUEST);
      }

      throw new ServiceException(ex.getFullMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
