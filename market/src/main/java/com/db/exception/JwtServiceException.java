package com.db.exception;

public class JwtServiceException extends Exception {

  public static final String ACCESS_TOKEN_NOT_FOUND = "Access token is not provided or provided in wrong way";
  public static final String WRONG_ACCESS_TOKEN_TYPE = "Wrong access token type";
  public static final String INVALID_ACCESS_TOKEN = "Provided access token is invalid";

  public JwtServiceException(String message) {
    super(message);
  }
}
