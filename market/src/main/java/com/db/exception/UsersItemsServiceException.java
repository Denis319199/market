package com.db.exception;

public class UsersItemsServiceException extends Exception {

  public static final String USERS_ITEM_NOT_FOUND = "User's item not found";
  public static final String USERS_ITEM_ALREADY_EXISTS = "User's item already exists";
  public static final String BAD_USER_ID =
      "Bad user id. Either an user doesn't exist or an user is disabled";

  public UsersItemsServiceException(String message) {
    super(message);
  }
}
