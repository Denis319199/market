package com.db.exception;

public class CountriesServiceException extends Exception {

  public static final String COUNTRY_ALREADY_EXISTS = "Country already exists";
  public static final String COUNTRY_NOT_FOUND = "Country not found";

  public CountriesServiceException(String message) {
    super(message);
  }
}
