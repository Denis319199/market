package com.db.utility.sql;

import com.db.utility.Utilities;
import com.db.utility.Recursive;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class SqlQueryExecutor {

  @FunctionalInterface
  interface GetColumnFunction<T> {
    T get(ResultSet resultSet, String name) throws Exception;
  }

  private static final Map<Class<?>, GetColumnFunction<?>> CONVERTERS;

  static {
    CONVERTERS = new HashMap<>();

    CONVERTERS.put(Boolean.class, ResultSet::getBoolean);
    CONVERTERS.put(boolean.class, ResultSet::getBoolean);

    CONVERTERS.put(Byte.class, ResultSet::getByte);
    CONVERTERS.put(byte.class, ResultSet::getByte);

    CONVERTERS.put(Short.class, ResultSet::getShort);
    CONVERTERS.put(short.class, ResultSet::getShort);

    CONVERTERS.put(Integer.class, ResultSet::getInt);
    CONVERTERS.put(int.class, ResultSet::getInt);

    CONVERTERS.put(Long.class, ResultSet::getLong);
    CONVERTERS.put(long.class, ResultSet::getLong);

    CONVERTERS.put(Float.class, ResultSet::getFloat);
    CONVERTERS.put(float.class, ResultSet::getFloat);

    CONVERTERS.put(BigDecimal.class, ResultSet::getBigDecimal);

    CONVERTERS.put(Instant.class, (resultSet, name) -> resultSet.getTimestamp(name).toInstant());

    CONVERTERS.put(LocalDate.class, ((resultSet, name) -> resultSet.getDate(name).toLocalDate()));

    CONVERTERS.put(Byte[].class, ResultSet::getObject);
    CONVERTERS.put(byte[].class, ResultSet::getBytes);

    CONVERTERS.put(String.class, ResultSet::getString);
  }

  private final SessionFactory sessionFactory;

  public SqlQueryExecutor(EntityManagerFactory entityManagerFactory) {
    sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
  }

  public <T> List<T> execute(String query, Class<T> clazz) {
    try (Session session = sessionFactory.openSession()) {
      List<T> result = new ArrayList<>();
      session.doWork(
          connection -> {
            try (Statement statement = connection.createStatement()) {
              ResultSet resultSet = statement.executeQuery(query);

              while (resultSet.next()) {
                result.add(convertTo(resultSet, clazz));
              }
            } catch (Exception ignore) {
            }
          });

      return result;
    }
  }

  private <T> T convertTo(ResultSet resultSet, Class<T> clazz) throws Exception {
    T instance = clazz.getDeclaredConstructor().newInstance();

    for (final Field field : clazz.getDeclaredFields()) {
      if (field.getAnnotation(Transient.class) != null) {
        continue;
      }

      StringBuilder setterName = new StringBuilder("set");
      setterName.append(field.getName());
      setterName.setCharAt(3, Character.toUpperCase(setterName.charAt(3)));

      try {
        if (field.getAnnotation(Recursive.class) == null) {
          clazz
              .getDeclaredMethod(setterName.toString(), field.getType())
              .invoke(instance, getValueFromResultSet(field, resultSet));
        } else {
          clazz
              .getDeclaredMethod(setterName.toString(), field.getType())
              .invoke(instance, convertTo(resultSet, field.getType()));
        }
      } catch (Exception ignore) {
      }
    }

    return instance;
  }

  private Object getValueFromResultSet(Field field, ResultSet resultSet) {
    try {
      Column column = field.getAnnotation(Column.class);
      Enumerated enumerated = field.getAnnotation(Enumerated.class);

      String columnName =
          column == null ? Utilities.translateFromCamelToSnakeCase(field.getName()) : column.name();

      Class<?> fieldType = field.getType();
      if (enumerated == null) {
        return CONVERTERS.get(fieldType).get(resultSet, columnName);
      } else if (EnumType.ORDINAL.equals(enumerated.value())) {

        return fieldType.getEnumConstants()[resultSet.getInt(columnName)];
      } else {
        return fieldType
            .getDeclaredMethod("valueOf", String.class)
            .invoke(null, resultSet.getString(columnName));
      }
    } catch (Exception ex) {
      return null;
    }
  }
}
