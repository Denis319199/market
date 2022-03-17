package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.User;
import com.db.model.dto.user.UserExtendedDto;
import com.db.model.dto.user.UserInsertDto;
import com.db.model.dto.user.UserExtendedUpdateDto;
import com.db.service.UsersService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersAdminController {
  private final UsersService usersService;
  private final ModelMapper modelMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<UserExtendedDto> getUsers(@Min(0) int page, @Min(1) int size) {
    return usersService.getUsers(page, size).stream()
        .map(user -> modelMapper.map(user, UserExtendedDto.class))
        .collect(Collectors.toList());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  UserExtendedDto createUser(@RequestBody @Valid UserInsertDto userDto) throws ServiceException {
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
  UserExtendedDto updateUser(@RequestBody @Valid UserExtendedUpdateDto userDto)
      throws ServiceException {
    try {
      User user = usersService.saveUser(modelMapper.map(userDto, User.class));
      return modelMapper.map(user, UserExtendedDto.class);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  void deleteUser(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      usersService.deleteUser(id);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
