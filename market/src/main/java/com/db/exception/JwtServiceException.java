package com.db.exception;

public class JwtServiceException extends Exception {

  public static String INVALID_TOKEN = "Invalid token";
  public static String TOKEN_NOT_FOUND = "Token not found";

  public JwtServiceException(String message) {
    super(message);
  }
}
