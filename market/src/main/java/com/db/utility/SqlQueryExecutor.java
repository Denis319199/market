package com.db.utility;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
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

    CONVERTERS.put(Boolean.class, ResultSet::getObject);
    CONVERTERS.put(boolean.class, ResultSet::getBoolean);

    CONVERTERS.put(Byte.class, ResultSet::getObject);
    CONVERTERS.put(byte.class, ResultSet::getByte);

    CONVERTERS.put(Short.class, ResultSet::getObject);
    CONVERTERS.put(short.class, ResultSet::getShort);

    CONVERTERS.put(Integer.class, ResultSet::getObject);
    CONVERTERS.put(int.class, ResultSet::getInt);

    CONVERTERS.put(Long.class, ResultSet::getObject);
    CONVERTERS.put(long.class, ResultSet::getLong);

    CONVERTERS.put(Float.class, ResultSet::getObject);
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
            }
          });

      return result;
    }
  }

  private String translateFromCamelToSnakeCase(String name) {
    StringBuilder builder = new StringBuilder(name);
    for (int i = 0; i < builder.length(); ++i) {
      char symbol = builder.charAt(i);
      if (Character.isUpperCase(symbol)) {
        builder.setCharAt(i, Character.toLowerCase(symbol));
        builder.insert(i, '_');
      }
    }

    return builder.toString();
  }

  private <T> T convertTo(ResultSet resultSet, Class<T> clazz) {
    try {
      T instance = clazz.getConstructor().newInstance();

      for (final Field field : clazz.getDeclaredFields()) {
        if (Objects.nonNull(field.getAnnotation(Transient.class))) {
          continue;
        }

        Column column = field.getAnnotation(Column.class);
        Enumerated enumerated = field.getAnnotation(Enumerated.class);

        String columnName =
            Objects.isNull(column) ? translateFromCamelToSnakeCase(field.getName()) : column.name();

        StringBuilder setterName = new StringBuilder("set");
        setterName.append(field.getName());
        setterName.setCharAt(3, Character.toUpperCase(setterName.charAt(3)));

        Class<?> fieldType = field.getType();

        if (Objects.isNull(enumerated)) {
          clazz
              .getDeclaredMethod(setterName.toString(), fieldType)
              .invoke(instance, CONVERTERS.get(fieldType).get(resultSet, columnName));
        } else if (EnumType.ORDINAL.equals(enumerated.value())) {
          clazz
              .getDeclaredMethod(setterName.toString(), fieldType)
              .invoke(instance, fieldType.getEnumConstants()[resultSet.getInt(columnName)]);
        } else {
          clazz
              .getDeclaredMethod(setterName.toString(), fieldType)
              .invoke(
                  instance,
                  fieldType
                      .getDeclaredMethod("valueOf", String.class)
                      .invoke(null, resultSet.getString(columnName)));
        }
      }

      return instance;
    } catch (Exception ignored) {
      return null;
    }
  }
}
