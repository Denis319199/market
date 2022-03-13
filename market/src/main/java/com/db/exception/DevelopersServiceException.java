package com.db.exception;

public class DevelopersServiceException extends Exception {

  public static final String DEVELOPER_NOT_FOUND = "Developer not found";
  public static final String DEVELOPER_ALREADY_EXISTS = "Developer already exists";

  public DevelopersServiceException(String message) {
    super(message);
  }
}
