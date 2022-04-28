package com.db.utility.filter.model;

public enum Operation {
  EQUAL("="),
  NOT_EQUAL("!="),
  LESS_THAN("<"),
  GREATER_THAN(">"),
  LESS_THAN_OR_EQUAL("<="),
  GREATER_THAN_OR_EQUAL(">="),
  LIKE("LIKE");

  private final String sign;

  Operation(String sign) {
    this.sign = sign;
  }

  public String getSign() {
    return sign;
  }

  public Operation getOpposite() {
    switch (this) {
      case EQUAL:
        return NOT_EQUAL;
      case NOT_EQUAL:
        return EQUAL;
      case LESS_THAN:
        return GREATER_THAN;
      case GREATER_THAN:
        return LESS_THAN;
      case LESS_THAN_OR_EQUAL:
        return GREATER_THAN_OR_EQUAL;
      case GREATER_THAN_OR_EQUAL:
        return LESS_THAN_OR_EQUAL;
    }
    return this;
  }
}
