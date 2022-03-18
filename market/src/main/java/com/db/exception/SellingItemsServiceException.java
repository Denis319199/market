package com.db.exception;

public class SellingItemsServiceException extends Exception {
  public static final String SELLING_ITEM_NOT_FOUND = "Selling item not found";
  public static final String SELLING_ITEM_ALREADY_EXISTS = "Selling item already exists";
  public static final String BAD_SELLER_ID =
      "Bad seller id. Either user doesn't exist or user is disabled";

  public SellingItemsServiceException(String message) {
    super(message);
  }
}
