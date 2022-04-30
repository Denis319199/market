package com.db.utility.validation.validator;

import com.db.utility.validation.annotation.Image;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
  @Override
  public void initialize(Image constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    String contentType = value.getContentType();
    if (contentType == null) {
      return false;
    }

    return contentType.equals(MediaType.IMAGE_PNG_VALUE) || contentType.equals(MediaType.IMAGE_JPEG_VALUE);
  }
}
