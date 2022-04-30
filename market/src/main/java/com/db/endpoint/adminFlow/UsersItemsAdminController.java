package com.db.endpoint.adminFlow;

import com.db.exception.ServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import com.db.model.dto.usersItem.UsersItemExtendedDto;
import com.db.model.dto.usersItem.UsersItemInsertDto;
import com.db.model.dto.usersItem.UsersItemUpdateDto;
import com.db.model.filter.UsersItemFilter;
import com.db.service.UsersItemsService;
import com.db.utility.filter.Filter;
import com.db.utility.filter.model.FilterResult;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user/item")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class UsersItemsAdminController {
  private final UsersItemsService usersItemsService;
  private final ModelMapper modelMapper;
  private final Filter filter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<UsersItemExtendedDto> getUsersItems(@RequestBody @Valid UsersItemFilter query)
      throws ServiceException {
    return filter.doFilter(query, UsersItemExtendedDto.class);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation
  public UsersItem insertUsersItem(@RequestBody @Valid UsersItemInsertDto usersItemDto)
      throws ServiceException {
    try {
      return usersItemsService.insertUsersItem(modelMapper.map(usersItemDto, UsersItem.class));
    } catch (UsersItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public UsersItem updateUsersItem(@RequestBody @Valid UsersItemUpdateDto usersItemDto)
      throws ServiceException {
    try {
      return usersItemsService.updateUsersItem(modelMapper.map(usersItemDto, UsersItem.class));
    } catch (UsersItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void deleteUsersItem(
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int userId,
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int itemId)
      throws ServiceException {
    try {
      usersItemsService.deleteUsersItem(new UsersItemId(userId, itemId));
    } catch (UsersItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
