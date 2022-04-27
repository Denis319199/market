package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.User;
import com.db.model.dto.user.UserExtendedDto;
import com.db.model.dto.user.UserUpdateDto;
import com.db.service.UsersService;
import com.db.utility.mapper.ModelMapper;
import com.db.utility.validation.annotation.GroupValid;
import com.db.utility.validation.group.PlainUserGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
@Validated
public class ThisUserController {

  private final UsersService usersService;
  private final ModelMapper modelMapper;

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation
  @ResponseStatus(HttpStatus.OK)
  public UserExtendedDto me(@Parameter(hidden = true) Authentication auth) throws ServiceException {
    try {
      return modelMapper.map(
          usersService.findUserById((Integer) auth.getPrincipal()), UserExtendedDto.class);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public UserExtendedDto updateUser(
      @RequestBody @GroupValid(PlainUserGroup.class) UserUpdateDto userDto,
      @Parameter(hidden = true) Authentication auth)
      throws ServiceException {
    User user = modelMapper.map(userDto, User.class);
    user.setId((Integer) auth.getPrincipal());

    try {
      return modelMapper.map(usersService.updateUser(user), UserExtendedDto.class);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
