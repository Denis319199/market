package com.db.service;

import com.db.app.configuration.properties.JwtClaimsProperties;
import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.JwtServiceRequestProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
  private final JwtProperties jwtProperties;
  private final JwtClaimsProperties jwtClaimsProperties;
  private final JwtServiceRequestProperties jwtServiceRequestProperties;

  public String createServiceRequestToken() {
    Date now = new Date();
    Date expiration =
        new Date(now.getTime() + jwtServiceRequestProperties.getExpirationTime() * 1000);

    return Jwts.builder()
        .claim(jwtClaimsProperties.getUserId(), jwtServiceRequestProperties.getUserId())
        .claim(jwtClaimsProperties.getRole(), jwtServiceRequestProperties.getRole())
        .claim(jwtClaimsProperties.getTokenType(), jwtProperties.getAccessTokenName())
        .setSubject(jwtServiceRequestProperties.getSubject())
        .setExpiration(expiration)
        .setIssuedAt(now)
        .signWith(jwtProperties.getKey())
        .compact();
  }

  public Claims validateAccessTokenAndGetClaims(String token) {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token);
      Claims claims = claimsJws.getBody();

      if (jwtProperties
          .getAccessTokenName()
          .equals(claims.get(jwtClaimsProperties.getTokenType()))) {
        return claims;
      }
      return null;
    } catch (JwtException ex) {
      return null;
    }
  }

  public int getUserId(Claims claims) {
    return (Integer) claims.get(jwtClaimsProperties.getUserId());
  }

  public String getRole(Claims claims) {
    return (String) claims.get(jwtClaimsProperties.getRole());
  }

  public String getAccessTokenFromRequest(HttpServletRequest request) {
    String headerValue = request.getHeader(jwtProperties.getHeader());
    if (Objects.isNull(headerValue)) {
      return null;
    }

    return headerValue.replaceFirst(jwtProperties.getTokenType() + " ", "");
  }
}
