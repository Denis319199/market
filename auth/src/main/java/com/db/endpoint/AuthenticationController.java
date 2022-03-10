package com.db.endpoint;

import com.db.exception.ServiceException;
import com.db.model.User;
import com.db.model.dto.AuthorizedUserDto;
import com.db.model.dto.LoginDto;
import com.db.model.dto.TokenDto;
import com.db.model.dto.user.UserDto;
import com.db.model.dto.user.UserInsertDto;
import com.db.service.JwtService;
import com.db.service.UsersService;
import io.jsonwebtoken.Claims;
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
    } catch (AuthenticationException ex) {
      throw new ServiceException(
          ServiceException.BAD_AUTHENTICATION + ": " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  TokenDto refreshToken(@RequestParam String token) throws ServiceException {
    Claims claims = jwtService.validateRefreshTokenAndGetClaims(token);

    if (Objects.isNull(claims)) {
      throw new ServiceException(ServiceException.INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
    }

    User user = usersService.findUserById(jwtService.getUserId(claims));
    return TokenDto.builder()
        .accessToken(jwtService.createAccessToken(user))
        .refreshToken(jwtService.createRefreshToken(user))
        .tokenType(jwtService.getTokenType())
        .build();
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  void logout(@RequestParam String token) throws ServiceException {
    Claims claims = jwtService.validateRefreshTokenAndGetClaims(token);
    if (Objects.isNull(claims)) {
      throw new ServiceException(ServiceException.INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(
      value = "/signup",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  AuthorizedUserDto signup(@RequestBody @Valid UserInsertDto userInsertDto)
      throws ServiceException {
    User user = usersService.insertUser(modelMapper.map(userInsertDto, User.class));

    UserDto userDto = modelMapper.map(user, UserDto.class);
    TokenDto tokenDto =
        TokenDto.builder()
            .accessToken(jwtService.createAccessToken(user))
            .refreshToken(jwtService.createRefreshToken(user))
            .tokenType(jwtService.getTokenType())
            .build();

    return new AuthorizedUserDto(userDto, tokenDto);
  }
}
