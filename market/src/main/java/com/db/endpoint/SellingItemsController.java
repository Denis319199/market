package com.db.endpoint;

import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.SellingItem;
import com.db.model.dto.sellingItem.SellingItemInsertDto;
import com.db.service.SellingItemsService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/selling")
@Validated
public class SellingItemsController {
  private final SellingItemsService sellingItemsService;
  private final ModelMapper modelMapper;

  @GetMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  @ResponseStatus(HttpStatus.OK)
  List<SellingItem> getAllSellingItems(
      @RequestParam @Min(0) int page,
      @RequestParam @Min(1) int size,
      @RequestParam(defaultValue = "false") Boolean isOwn,
      @RequestParam(required = false) Integer game,
      @RequestParam(defaultValue = "price") String orderBy,
      @RequestParam(defaultValue = "true") Boolean ascOrder,
      Authentication auth) {
    return sellingItemsService.getAllSellingItemsWithFilters(
        page, size, isOwn, (Integer) auth.getPrincipal(), game, orderBy, ascOrder);
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  @ResponseStatus(HttpStatus.OK)
  void sellItem(@RequestBody @Valid SellingItemInsertDto sellingItemDto, Authentication auth)
      throws ServiceException {
    SellingItem sellingItem = modelMapper.map(sellingItemDto, SellingItem.class);
    sellingItem.setSellerId((Integer) auth.getPrincipal());
    try {
      sellingItemsService.sellItem(sellingItem);
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/purchase")
  @PreAuthorize("hasRole('ROLE_USER')")
  @ResponseStatus(HttpStatus.OK)
  void purchaseItem(@RequestParam @Min(1) int itemId, @RequestParam @Min(1) int customerId) throws ServiceException {
    try {
      sellingItemsService.purchaseItem(itemId, customerId);
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  void removeItemFromSale(@RequestParam @Min(1) int id, Authentication auth)
      throws ServiceException {
    try {
      sellingItemsService.removeItemFromSale(id, (Integer) auth.getPrincipal());
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
