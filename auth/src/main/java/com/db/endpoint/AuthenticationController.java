package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.User;
import com.db.model.dto.AuthorizedUserDto;
import com.db.model.dto.LoginDto;
import com.db.model.dto.TokenDto;
import com.db.model.dto.user.UserDto;
import com.db.model.dto.user.UserInsertDto;
import com.db.service.JwtService;
import com.db.service.UsersService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
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
@Api
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
  @ApiOperation("")
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
    } catch (AuthenticationException | UsersServiceException ex) {
      throw new ServiceException(
          ServiceException.BAD_AUTHENTICATION + ": " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  TokenDto refreshToken(@RequestParam String token) throws ServiceException {
    Claims claims = jwtService.validateRefreshTokenAndGetClaims(token);

    if (Objects.isNull(claims)) {
      throw new ServiceException(ServiceException.INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
    }

    try {
      User user = usersService.findUserById(jwtService.getUserId(claims));
      if (user.getIsEnabled()) {
        return TokenDto.builder()
            .accessToken(jwtService.createAccessToken(user))
            .refreshToken(jwtService.createRefreshToken(user))
            .tokenType(jwtService.getTokenType())
            .build();
      }
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    throw new ServiceException(ServiceException.USER_IS_DISABLED, HttpStatus.FORBIDDEN);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  void logout(@RequestParam String token) throws ServiceException {
    Claims claims = jwtService.validateRefreshTokenAndGetClaims(token);
    if (Objects.isNull(claims)) {
      throw new ServiceException(ServiceException.INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("")
  void signup(@RequestBody @Valid UserInsertDto userInsertDto) throws ServiceException {
    try {
      usersService.insertUser(modelMapper.map(userInsertDto, User.class));
    } catch (UsersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
