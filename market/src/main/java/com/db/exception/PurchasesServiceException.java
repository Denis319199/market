package com.db.exception;

public class PurchasesServiceException extends Exception {
  public static final String PURCHASE_NOT_FOUND = "Purchase not found";
  public static final String PURCHASE_ALREADY_EXISTS = "Purchase already exists";
  public static final String BAD_SELLER_OR_CUSTOMER_ID =
      "Bad seller or customer id. Either users don't exits or users are disabled";

  public PurchasesServiceException(String message) {
    super(message);
  }
}
