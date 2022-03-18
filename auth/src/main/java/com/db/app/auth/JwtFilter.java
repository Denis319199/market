package com.db.app.auth;

import com.db.model.Role;
import com.db.service.JwtService;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

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
        UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
        if (user.isEnabled()) {
          SecurityContextHolder.getContext()
              .setAuthentication(
                  new UsernamePasswordAuthenticationToken(
                      jwtService.getUserId(claims), token, user.getAuthorities()));
        }
      }
    } catch (Exception ignored) {
    }

    chain.doFilter(request, response);
  }
}
