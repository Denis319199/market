package com.db.utility;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SqlQueryBuilder {
  private StringBuilder query = new StringBuilder();

  public static SqlQueryBuilder builder() {
    return new SqlQueryBuilder();
  }

  public SqlQueryBuilder select(String... what) {
    query.append("SELECT ");

    final int length = what.length - 1;
    for (int i = 0; i < length; ++i) {
      query.append(what[i]).append(", ");
    }

    query.append(what[length]);

    return this;
  }

  public SqlQueryBuilder from(String table) {
    query.append("\nFROM ").append(table);

    return this;
  }

  public SqlQueryBuilder fromQuery(String query) {
    this.query.append("\nFROM (\n").append(query).append("\n)");

    return this;
  }

  public SqlQueryBuilder as(String alias) {
    query.append(" AS ").append(alias);

    return this;
  }

  public SqlQueryBuilder on(String condition, Object... values) {
    query.append(" ON ").append(String.format(condition, values));

    return this;
  }

  public SqlQueryBuilder innerJoin(String table) {
    query.append(" INNER JOIN ").append(table);

    return this;
  }

  public SqlQueryBuilder innerJoinQuery(String query) {
    this.query.append(" INNER JOIN (\n").append(query).append("\n)");

    return this;
  }

  public SqlQueryBuilder where(String condition, Object... values) {
    query.append("\nWHERE ").append(String.format(condition, values));

    return this;
  }

  public SqlQueryBuilder and(String condition, Object... values) {
    query.append(" AND ").append(String.format(condition, values));

    return this;
  }

  public SqlQueryBuilder or(String condition, Object... values) {
    query.append(" OR ").append(String.format(condition, values));

    return this;
  }

  public SqlQueryBuilder orderBy(String[] what, Boolean[] asc) {
    query.append("\nORDER BY ");

    query.append(what[0]);
    if (!asc[0]) {
      query.append(" DESC");
    }

    for (int i = 1; i < asc.length; ++i) {
      query.append(", ").append(what[i]);
      if (!asc[i]) {
        query.append(" DESC");
      }
    }

    return this;
  }

  public SqlQueryBuilder limit(int num) {
    query.append("\nLIMIT ").append(num);

    return this;
  }

  public SqlQueryBuilder offset(int num) {
    query.append("\nOFFSET ").append(num);

    return this;
  }

  public SqlQueryBuilder append(String str, Object... values) {
    query.append(String.format(str, values));

    return this;
  }

  public String build() {
    return query.toString();
  }
}
