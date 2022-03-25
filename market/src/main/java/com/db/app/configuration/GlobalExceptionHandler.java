package com.db.app.configuration;

import com.db.exception.ServiceException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
  @Value("${exception-field}")
  private String errorField;

  @ExceptionHandler(ServiceException.class)
  ResponseEntity<Map<String, String>> handleServiceException(ServiceException ex) {
    Map<String, String> response = new HashMap<>();
    response.put(errorField, ex.getMessage());
    return ResponseEntity.status(ex.getHttpStatus().value()).body(response);
  }

  @ExceptionHandler({
    HttpMessageConversionException.class,
    MissingServletRequestParameterException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> handleHttpMessageConversionException(Exception ex) {
    return prepareResponse(ex);
  }

  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  Map<String, String> handleAccessDeniedException(Exception ex) {
    return prepareResponse(ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  Map<String, String> handleException(Exception ex) {
    return prepareResponse(ex);
  }

  private Map<String, String> prepareResponse(Exception ex) {
    Map<String, String> response = new HashMap<>();
    response.put(errorField, ex.getMessage());
    return response;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleMethodArgumentsValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put(errorField, "");

    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.merge(errorField, fieldName + ": " + errorMessage + "; ", String::concat);
            });

    return errors;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException exception) {
    Map<String, String> response = new HashMap<>();

    if (exception.getConstraintViolations().isEmpty()) {
      response.put(errorField, "Server error");
      return ResponseEntity.internalServerError().body(response);
    } else {
      ConstraintViolation<?> violation = exception.getConstraintViolations().iterator().next();
      String fieldName = violation.getPropertyPath().toString().split("[.]")[1];
      response.put(errorField, fieldName + ": " + violation.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> handleBindException(BindException exception) {
    StringBuilder message = new StringBuilder();
    for (FieldError error : exception.getFieldErrors()) {
      message.append(error.getField());
      message.append(": ");
      String errorMessage = error.getDefaultMessage();
      message.append(
          Objects.requireNonNullElse(errorMessage, "constraint failed, please, check value"));
    }

    Map<String, String> response = new HashMap<>();
    response.put(errorField, message.toString());
    return response;
  }
}
