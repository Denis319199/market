package com.db.endpoint;

import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.SellingItem;
import com.db.model.dto.sellingItem.SellingItemInsertDto;
import com.db.model.dto.sellingItem.SellingItemUpdateDto;
import com.db.service.SellingItemsService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/selling")
@Validated
public class SellingItemsController {
  private final SellingItemsService sellingItemsService;
  private final ModelMapper modelMapper;

  @GetMapping
  List<SellingItem> getAllSellingItems(
      @RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
    return sellingItemsService.getAllSellingItems(page, size);
  }

  @PostMapping
  SellingItem insertSellingItem(@RequestBody @Valid SellingItemInsertDto sellingItemDto)
      throws ServiceException {
    try {
      return sellingItemsService.insertSellingItem(
          modelMapper.map(sellingItemDto, SellingItem.class));
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  SellingItem updateSellingItem(@RequestBody @Valid SellingItemUpdateDto sellingItemDto)
      throws ServiceException {
    try {
      return sellingItemsService.updateSellingItem(
          modelMapper.map(sellingItemDto, SellingItem.class));
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  void deleteSellingItem(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      sellingItemsService.deleteSellingItem(id);
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
