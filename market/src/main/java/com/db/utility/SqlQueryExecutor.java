package com.db.utility;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class SqlQueryExecutor {

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

  private <T> T convertTo(ResultSet resultSet, Class<T> clazz) {
    try {
      T instance = clazz.getConstructor().newInstance();

      for (final Field field : clazz.getDeclaredFields()) {
        Column column = field.getAnnotation(Column.class);

        StringBuilder setterName = new StringBuilder("set");
        setterName.append(field.getName());
        setterName.replace(3, 4, String.valueOf(setterName.charAt(3)).toUpperCase(Locale.ROOT));
        clazz.getDeclaredMethod(setterName.toString(), field.getType()).invoke(instance, resultSet.getObject(column.name()));
      }

      return instance;
    } catch (Exception ignored) {
      return null;
    }
  }
}
