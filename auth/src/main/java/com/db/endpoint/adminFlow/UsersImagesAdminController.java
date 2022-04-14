package com.db.endpoint.adminFlow;

import com.db.exception.ServiceException;

import com.db.exception.UsersServiceException;
import com.db.model.UsersImage;
import com.db.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/user/image")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Api
public class UsersImagesAdminController {
  private final UsersService usersService;

  @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  byte[] getImage(@Min(1) int id) throws ServiceException {
    try {
      return usersService.getUsersImage(id);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("")
  void insertImage(@RequestParam MultipartFile image, @RequestParam @Min(1) int userId)
      throws ServiceException, IOException {
    try {
      usersService.insertImage(new UsersImage(userId, image.getBytes()));
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  void updateImage(@RequestParam MultipartFile image, @RequestParam @Min(1) int userId)
      throws ServiceException, IOException {
    try {
      usersService.updateImage(new UsersImage(userId, image.getBytes()));
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  void deleteImage(@RequestParam @Min(1) int userId) throws ServiceException {
    try {
      usersService.deleteImage(userId);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
