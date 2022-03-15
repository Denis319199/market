package com.db.exception;

public class PurchasesServiceException extends Exception {
  public static final String PURCHASE_NOT_FOUND = "Purchase not found";
  public static final String PURCHASE_ALREADY_EXISTS = "Purchase already exists";

  public PurchasesServiceException(String message) {
    super(message);
  }
}
