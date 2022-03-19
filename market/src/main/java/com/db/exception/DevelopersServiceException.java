package com.db.exception;

public class DevelopersServiceException extends Exception {

  public static final String DEVELOPER_NOT_FOUND = "Developer not found";
  public static final String DEVELOPER_ALREADY_EXISTS = "Developer already exists";
  public static final String BAD_COUNTRY_ID = "Bad county id. Country doesn't exist";

  public DevelopersServiceException(String message) {
    super(message);
  }
}
