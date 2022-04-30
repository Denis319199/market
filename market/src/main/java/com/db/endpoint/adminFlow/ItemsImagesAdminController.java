package com.db.endpoint.adminFlow;

import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.ItemsImage;
import com.db.service.ItemsService;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.annotation.Image;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/item/image")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class ItemsImagesAdminController {

  private final ItemsService itemsService;

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void insertItemsImage(
      @RequestParam @Image MultipartFile image, @RequestParam @Min(1) int itemId)
      throws IOException, ServiceException {
    try {
      itemsService.putItemsImage(new ItemsImage(itemId, image.getBytes()));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void deleteItemsImage(
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int itemId)
      throws ServiceException {
    try {
      itemsService.deleteItemsImage(itemId);
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
