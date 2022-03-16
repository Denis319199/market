package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import com.db.model.dto.usersItem.UsersItemInsertDto;
import com.db.model.dto.usersItem.UsersItemUpdateDto;
import com.db.service.UsersItemsService;
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
@RequestMapping("/users/items")
@RequiredArgsConstructor
@Validated
public class UsersItemsController {

  private final UsersItemsService usersItemsService;
  private final ModelMapper modelMapper;

  @GetMapping
  List<UsersItem> getAllUsersItems(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
    return usersItemsService.getAllUsersItems(page, size);
  }

  @PostMapping
  UsersItem insertUsersItem(@RequestBody @Valid UsersItemInsertDto usersItemDto)
      throws ServiceException {
    try {
      return usersItemsService.insertUsersItem(modelMapper.map(usersItemDto, UsersItem.class));
    } catch (UsersItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  UsersItem updateUsersItem(@RequestBody @Valid UsersItemUpdateDto usersItemDto)
      throws ServiceException {
    try {
      return usersItemsService.updateUsersItem(modelMapper.map(usersItemDto, UsersItem.class));
    } catch (UsersItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  public void deleteUsersItem(@RequestBody @Valid UsersItemId id) throws ServiceException {
    try {
      usersItemsService.deleteUsersItem(id);
    } catch (UsersItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
