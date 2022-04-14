package com.db.endpoint.adminFlow;

import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.User;
import com.db.model.dto.user.UserExtendedDto;
import com.db.model.dto.user.UserInsertDto;
import com.db.model.dto.user.UserUpdateDto;
import com.db.service.UsersService;
import com.db.utility.validation.annotation.GroupValid;
import com.db.utility.validation.group.AdminGroup;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersAdminController {
  private final UsersService usersService;
  private final ModelMapper modelMapper;

  @PostMapping("/existence")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  List<Boolean> checkExistence(
      @RequestBody List<Integer> usersList,
      @RequestParam(defaultValue = "false") Boolean onlyEnabled) {
    return usersList.stream()
        .map(id -> usersService.checkExistence(id, onlyEnabled))
        .collect(Collectors.toList());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  List<UserExtendedDto> getUsers(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
    return usersService.getUsers(page, size).stream()
        .map(user -> modelMapper.map(user, UserExtendedDto.class))
        .collect(Collectors.toList());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "")
  UserExtendedDto createUser(@RequestBody @GroupValid(AdminGroup.class) UserInsertDto userDto) throws ServiceException {
    try {
      User user = usersService.insertUser(modelMapper.map(userDto, User.class));
      return modelMapper.map(user, UserExtendedDto.class);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  UserExtendedDto updateUser(@RequestBody @GroupValid(AdminGroup.class) UserUpdateDto userDto)
      throws ServiceException {
    try {
      User user = usersService.updateUser(modelMapper.map(userDto, User.class));
      return modelMapper.map(user, UserExtendedDto.class);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  void deleteUser(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      usersService.deleteUser(id);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
