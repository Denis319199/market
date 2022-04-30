package com.db.endpoint.adminFlow;

import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.Item;
import com.db.model.dto.item.ItemInsertDto;
import com.db.model.dto.item.ItemUpdateDto;
import com.db.service.ItemsService;
import com.db.utility.mapper.ModelMapper;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/item")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class ItemsAdminController {

  private final ItemsService itemsService;
  private final ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation
  public Item insertItem(@RequestBody @Valid ItemInsertDto itemDto) throws ServiceException {
    try {
      return itemsService.insertItem(modelMapper.map(itemDto, Item.class));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public Item updateItem(@RequestBody @Valid ItemUpdateDto itemDto) throws ServiceException {
    try {
      return itemsService.updateItem(modelMapper.map(itemDto, Item.class));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void deleteItem(@RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int id)
      throws ServiceException {
    try {
      itemsService.deleteItem(id);
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
