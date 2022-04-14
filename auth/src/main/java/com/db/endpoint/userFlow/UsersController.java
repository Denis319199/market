package com.db.endpoint.userFlow;

import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.User;
import com.db.model.dto.user.UserExtendedDto;
import com.db.model.dto.user.UserUpdateDto;
import com.db.service.UsersService;
import com.db.utility.validation.annotation.GroupValid;
import com.db.utility.validation.group.PlainUserGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Api
public class UsersController {
  private final UsersService usersService;
  private final ModelMapper modelMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  UserExtendedDto getUser(Authentication auth) throws ServiceException {
    try {
      User user = usersService.findUserById((Integer) auth.getPrincipal());
      return modelMapper.map(user, UserExtendedDto.class);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  UserExtendedDto updateUser(@RequestBody @GroupValid(PlainUserGroup.class) UserUpdateDto userDto, Authentication auth)
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
