package com.db.service.impl;

import com.db.app.configuration.properties.JwtClaimsProperties;
import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.JwtServiceRequestProperties;
import com.db.exception.JwtServiceException;
import com.db.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile({"prod", "secured"})
public class JwtServiceImpl implements JwtService {
  protected final JwtProperties jwtProperties;
  protected final JwtClaimsProperties jwtClaimsProperties;
  protected final JwtServiceRequestProperties jwtServiceRequestProperties;

  @Override
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

  @Override
  public Claims validateAccessTokenAndGetClaims(String token) throws JwtServiceException {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token);
      Claims claims = claimsJws.getBody();

      if (!jwtProperties
          .getAccessTokenName()
          .equals(claims.get(jwtClaimsProperties.getTokenType()))) {
        throw new JwtServiceException(JwtServiceException.INVALID_ACCESS_TOKEN);
      }

      return claims;
    } catch (JwtException ex) {
      throw new JwtServiceException(JwtServiceException.INVALID_ACCESS_TOKEN);
    }
  }

  @Override
  public int getUserId(Claims claims) {
    return (Integer) claims.get(jwtClaimsProperties.getUserId());
  }

  @Override
  public String getRole(Claims claims) {
    return (String) claims.get(jwtClaimsProperties.getRole());
  }

  @Override
  public String getAccessTokenFromRequest(final HttpServletRequest request)
      throws JwtServiceException {
    final String tokenWithType = request.getHeader(jwtProperties.getHeader());

    if (tokenWithType == null) {
      throw new JwtServiceException(JwtServiceException.ACCESS_TOKEN_NOT_FOUND);
    }

    final String tokenType = jwtProperties.getTokenType();

    if (!tokenWithType.startsWith(tokenType)) {
      throw new JwtServiceException(JwtServiceException.WRONG_ACCESS_TOKEN_TYPE);
    }

    return tokenWithType.replaceFirst(tokenType + " ", "");
  }
}
