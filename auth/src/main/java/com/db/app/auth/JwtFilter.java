package com.db.app.auth;

import com.db.app.configuration.properties.UnsecuredEndpointsProperties;
import com.db.exception.JwtServiceException;
import com.db.exception.ServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.Role;
import com.db.model.User;
import com.db.service.JwtService;
import com.db.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UsersService usersService;
  private final UnsecuredEndpointsProperties unsecuredEndpointsProperties;

  @Value("${exception-field}")
  private String errorField;

  private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      String token = jwtService.getAccessTokenFromRequest(request);
      Claims claims = jwtService.validateAccessTokenAndGetClaims(token);

      if (jwtService.isServiceRequest(claims)) {
        SecurityContextHolder.getContext()
            .setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    jwtService.getUserId(claims),
                    token,
                    List.of(new SimpleGrantedAuthority(jwtService.getRole(claims)))));
      } else {
        int userId = jwtService.getUserId(claims);
        User user = usersService.findUserById(userId);
        if (!user.getIsEnabled()) {
          rejectRequest(response, HttpStatus.FORBIDDEN, ServiceException.USER_IS_DISABLED);
          return;
        }
        SecurityContextHolder.getContext()
            .setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    userId, token, List.of(new SimpleGrantedAuthority(user.getRole().name()))));
      }
    } catch (JwtServiceException | UsersServiceException ex) {
      rejectRequest(response, HttpStatus.UNAUTHORIZED, ServiceException.USER_IS_DISABLED);
      return;
    }

    chain.doFilter(request, response);
  }

  private void rejectRequest(HttpServletResponse response, HttpStatus status, String errorMessage)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), Map.of(errorField, errorMessage));
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    for (String unsecuredUri : unsecuredEndpointsProperties.getEndpoints()) {
      if (antPathMatcher.match(unsecuredUri, request.getRequestURI())) {
        return true;
      }
    }
    return false;
  }
}
