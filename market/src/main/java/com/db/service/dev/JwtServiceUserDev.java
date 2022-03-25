package com.db.service.dev;

import com.db.app.configuration.properties.JwtClaimsProperties;
import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.JwtServiceRequestProperties;
import com.db.exception.JwtServiceException;
import com.db.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("unsecured-user")
public class JwtServiceUserDev extends JwtServiceImpl {
  public JwtServiceUserDev(
      JwtProperties jwtProperties,
      JwtClaimsProperties jwtClaimsProperties,
      JwtServiceRequestProperties jwtServiceRequestProperties) {
    super(jwtProperties, jwtClaimsProperties, jwtServiceRequestProperties);
  }

  @Override
  public String getAccessTokenFromRequest(HttpServletRequest request) throws JwtServiceException {
    Date now = new Date();
    Date expiration =
        new Date(now.getTime() + jwtServiceRequestProperties.getExpirationTime() * 1000);

    return Jwts.builder()
        .claim(jwtClaimsProperties.getUserId(), jwtServiceRequestProperties.getUserId())
        .claim(jwtClaimsProperties.getRole(), "ROLE_USER")
        .claim(jwtClaimsProperties.getTokenType(), jwtProperties.getAccessTokenName())
        .setSubject(jwtServiceRequestProperties.getSubject())
        .setExpiration(expiration)
        .setIssuedAt(now)
        .signWith(jwtProperties.getKey())
        .compact();
  }
}
