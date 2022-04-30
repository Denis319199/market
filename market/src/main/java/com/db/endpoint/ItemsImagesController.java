package com.db.endpoint;

import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.service.ItemsService;
import com.db.utility.validation.ConstraintMessages;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/item/image")
@RequiredArgsConstructor
public class ItemsImagesController {
  private final ItemsService itemsService;

  @GetMapping(
      value = "/{itemId}",
      produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  byte[] getItemsImage(@PathVariable @Min(value = 1, message = ConstraintMessages.MIN) int itemId)
      throws ServiceException {
    try {
      return itemsService.getItemsImage(itemId).getImage();
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
