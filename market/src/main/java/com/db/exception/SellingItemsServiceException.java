package com.db.exception;

public class SellingItemsServiceException extends Exception {
  public static final String SELLING_ITEM_NOT_FOUND = "Selling item not found";
  public static final String BAD_SELLER_ID =
      "Bad seller id. Either user doesn't exist or user is disabled";
  public static final String USER_IS_NOT_AN_OWNER = "User is not an owner";
  public static final String USER_CANT_BUY_THEIR_OWN_ITEM = "User can't buy their own item";

  public SellingItemsServiceException(String message) {
    super(message);
  }
}
