package com.db.endpoint.userFlow;

import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.SellingItem;
import com.db.model.dto.sellingItem.SellingItemInsertDto;
import com.db.service.SellingItemsService;
import com.db.utility.mapper.ModelMapper;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/item")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public class OperationsOnItemsUserController {
  private final SellingItemsService sellingItemsService;
  private final ModelMapper modelMapper;

  @PostMapping("/sell")
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void sellItem(@Valid SellingItemInsertDto sellingItemDto, Authentication auth)
      throws ServiceException {
    SellingItem sellingItem = modelMapper.map(sellingItemDto, SellingItem.class);
    sellingItem.setSellerId((Integer) auth.getPrincipal());
    try {
      sellingItemsService.sellItemWithoutUserCheck(sellingItem);
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/purchase")
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void purchaseItem(
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int sellingItemId,
      @Parameter(hidden = true) Authentication auth)
      throws ServiceException {
    try {
      sellingItemsService.purchaseItemWithoutUserCheck(
          sellingItemId, (Integer) auth.getPrincipal());
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/withdraw")
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void removeItemFromSale(
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int id,
      @Parameter(hidden = true) Authentication auth)
      throws ServiceException {
    try {
      sellingItemsService.removeItemFromSale(id, (Integer) auth.getPrincipal());
    } catch (SellingItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
