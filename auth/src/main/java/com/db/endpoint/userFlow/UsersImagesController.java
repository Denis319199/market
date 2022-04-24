package com.db.endpoint.userFlow;

import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.UsersImage;
import com.db.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/image")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_USER')")
public class UsersImagesController {
  private final UsersService usersService;

  @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  byte[] getImage(@Parameter(hidden = true) Authentication auth) throws ServiceException {
    try {
      return usersService.getUsersImage((Integer) auth.getPrincipal());
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  void putImage(@RequestParam MultipartFile image, @Parameter(hidden = true) Authentication auth)
      throws ServiceException, IOException {
    try {
      usersService.putImage(new UsersImage((Integer) auth.getPrincipal(), image.getBytes()));
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  void deleteImage(@Parameter(hidden = true) Authentication auth) throws ServiceException {
    try {
      usersService.deleteImage((Integer) auth.getPrincipal());
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
