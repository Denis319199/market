package com.db.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends Exception {

    final private HttpStatus httpStatus;

    public final static String USER_NOT_FOUND = "User not found";
    public final static String BAD_AUTHENTICATION = "Bad authentication";
    public final static String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public final static String USER_ALREADY_EXISTS = "User already exists";
    public final static String COUNTRY_ALREADY_EXISTS = "Country already exists";
    public final static String COUNTRY_NOT_FOUND = "Country not found";
    public final static String IMAGE_NOT_FOUND = "Image not found";
    public final static String IMAGE_ALREADY_EXISTS = "Image already exists";

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
