package com.db.utility.validation.validator;

import com.db.utility.validation.annotation.GroupValid;
import com.db.utility.validation.annotation.Recursive;
import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class GroupValidValidator implements ConstraintValidator<GroupValid, Object> {
  private Class<?>[] groups;
  private static final Validator validator;

  static {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (!validator.validate(value, groups).isEmpty()) {
      return false;
    }
    if (!validator.validate(value, Default.class).isEmpty()) {
      return false;
    }
    return validateFields(value);
  }

  private boolean validateFields(Object value) {
    try {
      Class<?> clazz = value.getClass();

      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Recursive.class)) {
          StringBuilder getterName = new StringBuilder("get");
          getterName.append(field.getName());
          getterName.setCharAt(3, Character.toUpperCase(getterName.charAt(3)));

          Object getterResult = clazz.getMethod(getterName.toString()).invoke(value);

          if (!validateFields(getterResult)) {
            return false;
          }

          if (!validator.validate(getterResult, groups).isEmpty()) {
            return false;
          }

          if (!validator.validate(getterResult, Default.class).isEmpty()) {
            return false;
          }
        }
      }

      return true;

    } catch (Exception ignore) {
      return false;
    }
  }

  @Override
  public void initialize(GroupValid constraintAnnotation) {
    groups = constraintAnnotation.value();
  }
}
