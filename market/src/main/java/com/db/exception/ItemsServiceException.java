package com.db.exception;

public class ItemsServiceException extends Exception {

  public static final String ITEM_NOT_FOUND = "Item not found";
  public static final String ITEM_ALREADY_EXISTS = "Item already exists";
  public static final String ITEMS_IMAGE_NOT_FOUND = "Item's image not found";
  public static final String ITEMS_IMAGE_ALREADY_EXISTS = "Item's image already exists";

  public ItemsServiceException(String message) {
    super(message);
  }
}
