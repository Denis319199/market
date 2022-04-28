package com.db.utility.validation.validator;


import com.db.utility.validation.annotation.MinArray;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinArrayValidator implements ConstraintValidator<MinArray, Number[]> {

  private long minVal;

  @Override
  public void initialize(MinArray minAnnotation) {
    minVal = minAnnotation.value();
  }

  @Override
  public boolean isValid(Number[] values, ConstraintValidatorContext context) {
    if (values == null) {
      return true;
    }

    for (Number val : values) {
      if (val.longValue() < minVal) {
        return false;
      }
    }

    return true;
  }
}
