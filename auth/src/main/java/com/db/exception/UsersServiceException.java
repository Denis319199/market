package com.db.exception;

public class UsersServiceException extends Exception {

  public static final String USER_NOT_FOUND = "User not found";
  public static final String USER_ALREADY_EXISTS = "User already exists";
  public static final String IMAGE_NOT_FOUND = "Image not found";
  public static final String IMAGE_ALREADY_EXISTS = "Image already exists";

  public UsersServiceException(String message) {
    super(message);
  }
}
