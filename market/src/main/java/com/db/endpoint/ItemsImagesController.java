package com.db.endpoint;

import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.ItemsImage;
import com.db.service.ItemsService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping("/items/images")
@RequiredArgsConstructor
public class ItemsImagesController {
  private final ItemsService itemsService;

  @GetMapping(
      value = "/{itemId}",
      produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  byte[] getItemsImage(@PathVariable @Min(1) int itemId) throws ServiceException {
    try {
      return itemsService.getItemsImage(itemId).getImage();
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  void insertItemsImage(@RequestParam MultipartFile image, @RequestParam @Min(1) int itemId)
      throws IOException, ServiceException {
    try {
      itemsService.insertItemsImage(new ItemsImage(itemId, image.getBytes()));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  void updateItemsImage(@RequestParam MultipartFile image, @RequestParam @Min(1) int itemId)
      throws IOException, ServiceException {
    try {
      itemsService.updateItemsImage(new ItemsImage(itemId, image.getBytes()));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  void deleteItemsImage(@RequestParam @Min(1) int itemId) throws ServiceException {
    try {
      itemsService.deleteItemsImage(itemId);
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
