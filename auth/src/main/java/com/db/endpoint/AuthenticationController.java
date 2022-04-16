package com.db.endpoint;

import com.db.exception.JwtServiceException;
import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.Role;
import com.db.model.User;
import com.db.model.dto.user.AuthorizedUserDto;
import com.db.model.dto.auth.LoginDto;
import com.db.model.dto.auth.TokenDto;
import com.db.model.dto.user.UserDto;
import com.db.model.dto.user.UserInsertDto;
import com.db.service.JwtService;
import com.db.service.UsersService;
import com.db.utility.validation.annotation.GroupValid;
import com.db.utility.validation.group.PlainUserGroup;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

  private final UsersService usersService;
  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final ModelMapper modelMapper;

  @PostMapping(
      value = "/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  AuthorizedUserDto authenticate(@RequestBody @Valid LoginDto loginDto) throws ServiceException {
    try {
      Authentication auth =
          authManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginDto.getUsername(), loginDto.getPassword()));
      User user = usersService.findUserByUsername(auth.getName());
      UserDto userDto = modelMapper.map(user, UserDto.class);
      TokenDto tokenDto =
          TokenDto.builder()
              .accessToken(jwtService.createAccessToken(user))
              .refreshToken(jwtService.createRefreshToken(user))
              .tokenType(jwtService.getTokenType())
              .build();
      return new AuthorizedUserDto(userDto, tokenDto);
    } catch (DisabledException ex) {
      throw new ServiceException(ServiceException.USER_IS_DISABLED, HttpStatus.FORBIDDEN);
    } catch (AuthenticationException | UsersServiceException | JwtServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  TokenDto refreshToken(@RequestParam String token) throws ServiceException {
    try {
      Claims claims = jwtService.validateRefreshTokenAndGetClaims(token);

      User user = usersService.findUserById(jwtService.getUserId(claims));

      if (!user.getIsEnabled()) {
        throw new ServiceException(ServiceException.USER_IS_DISABLED, HttpStatus.FORBIDDEN);
      }

      return TokenDto.builder()
          .accessToken(jwtService.createAccessToken(user))
          .refreshToken(jwtService.createRefreshToken(user))
          .tokenType(jwtService.getTokenType())
          .build();
    } catch (JwtServiceException | UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  void logout(@RequestParam String token) throws ServiceException {
    try {
      jwtService.validateRefreshTokenAndGetClaims(token);
    } catch (JwtServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  void signup(@RequestBody @GroupValid(PlainUserGroup.class) UserInsertDto userExtendedInsertDto)
      throws ServiceException {
    try {
      User user = modelMapper.map(userExtendedInsertDto, User.class);

      user.setIsEnabled(true);
      user.setRole(Role.ROLE_USER);

      usersService.insertUser(user);
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
