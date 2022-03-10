package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.model.UsersImage;
import com.db.service.UsersService;
import java.io.IOException;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/users/images")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_USER')")
public class UsersImagesController {
  private final UsersService usersService;

  @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  byte[] getImage(Authentication auth) throws ServiceException {
    return usersService.getUsersImage((Integer) auth.getPrincipal());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  void insertImage(@RequestParam MultipartFile image, Authentication auth)
      throws ServiceException, IOException {
    usersService.insertImage(new UsersImage((Integer) auth.getPrincipal(), image.getBytes()));
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  void updateImage(@RequestParam MultipartFile image, Authentication auth)
      throws ServiceException, IOException {
    usersService.updateImage(new UsersImage((Integer) auth.getPrincipal(), image.getBytes()));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  void deleteImage(Authentication auth) {
    usersService.deleteImage((Integer) auth.getPrincipal());
  }
}
