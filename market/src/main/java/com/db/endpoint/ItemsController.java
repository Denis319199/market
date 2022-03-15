package com.db.endpoint;

import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.Item;
import com.db.model.dto.item.ItemInsertDto;
import com.db.model.dto.item.ItemUpdateDto;
import com.db.service.ItemsService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {
  private final ItemsService itemsService;
  private final ModelMapper modelMapper;

  @GetMapping
  List<Item> getAllItems(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
    return itemsService.getAllItems(page, size);
  }

  @GetMapping("/{id}")
  Item getItem(@PathVariable @Min(1) int id) throws ServiceException {
    try {
      return itemsService.findItemById(id);
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  Item insertItem(@RequestBody @Valid ItemInsertDto itemDto) throws ServiceException {
    try {
      return itemsService.insertItem(modelMapper.map(itemDto, Item.class));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  Item updateItem(@RequestBody @Valid ItemUpdateDto itemDto) throws ServiceException {
    try {
      return itemsService.updateItem(modelMapper.map(itemDto, Item.class));
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  void deleteItem(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      itemsService.deleteItem(id);
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
