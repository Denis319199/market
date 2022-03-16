package com.db.exception;

public class UsersItemsServiceException extends Exception {

  public static final String USERS_ITEM_NOT_FOUND = "User's item not found";
  public static final String USERS_ITEM_ALREADY_EXISTS = "User's item already exists";

  public UsersItemsServiceException(String message) {
    super(message);
  }
}
