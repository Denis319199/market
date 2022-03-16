package com.db.exception;

public class SellingItemsServiceException extends Exception {
  public static final String SELLING_ITEM_NOT_FOUND = "Selling item not found";
  public static final String SELLING_ITEM_ALREADY_EXISTS = "Selling item already exists";

  public SellingItemsServiceException(String message) {
    super(message);
  }
}
