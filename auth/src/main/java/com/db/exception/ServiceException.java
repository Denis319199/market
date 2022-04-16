package com.db.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends Exception {

  private final HttpStatus httpStatus;

  public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
  public static final String USER_IS_DISABLED = "User is disabled";

  public ServiceException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
