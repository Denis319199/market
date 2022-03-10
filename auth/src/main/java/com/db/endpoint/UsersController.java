package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.model.User;
import com.db.model.dto.user.UserExtendedDto;
import com.db.model.dto.user.UserUpdateDto;
import com.db.service.UsersService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UsersController {
  private final UsersService usersService;
  private final ModelMapper modelMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  UserExtendedDto getUser(Authentication auth) {
    User user = usersService.findUserById((Integer) auth.getPrincipal());
    return modelMapper.map(user, UserExtendedDto.class);
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  UserExtendedDto updateUser(@RequestBody @Valid UserUpdateDto userDto, Authentication auth)
      throws ServiceException {
    User user = modelMapper.map(userDto, User.class);
    user.setId((Integer) auth.getPrincipal());
    return modelMapper.map(usersService.saveUser(user), UserExtendedDto.class);
  }
}
