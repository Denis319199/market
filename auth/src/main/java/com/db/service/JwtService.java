package com.db.service;

import com.db.app.configuration.properties.JwtClaimsProperties;
import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.JwtServiceRequestProperties;
import com.db.exception.JwtServiceException;
import com.db.exception.UsersServiceException;
import com.db.model.RefreshToken;
import com.db.model.Role;
import com.db.model.User;
import com.db.repo.RefreshTokensRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
public class JwtService {
  private final JwtProperties jwtProperties;
  private final JwtClaimsProperties jwtClaimsProperties;
  private final JwtServiceRequestProperties jwtServiceRequestProperties;
  private final RefreshTokensRepo refreshTokensRepo;

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public String createRefreshToken(User user) throws JwtServiceException {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + jwtProperties.getRefreshExpirationTime() * 1000);

    String tokenId = UUID.randomUUID().toString();
    String token =
        Jwts.builder()
            .claim(jwtClaimsProperties.getUserId(), user.getId())
            .claim(jwtClaimsProperties.getRole(), user.getRole().name())
            .claim(jwtClaimsProperties.getTokenType(), jwtProperties.getRefreshTokenName())
            .setSubject(user.getUsername())
            .setExpiration(expiration)
            .setIssuedAt(now)
            .setId(tokenId)
            .signWith(jwtProperties.getKey())
            .compact();

    if (refreshTokensRepo.countUsersTokens(user.getId())
        >= jwtProperties.getMaxRefreshTokenNumber()) {
      throw new JwtServiceException(JwtServiceException.REFRESH_TOKEN_NUMBER_EXCEEDED);
    }

    refreshTokensRepo.save(
        RefreshToken.builder()
            .id(tokenId)
            .userid(user.getId())
            .token(token)
            .expiresOn(expiration.toInstant())
            .build());

    return token;
  }

  public String createAccessToken(User user) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + jwtProperties.getAccessExpirationTime() * 1000);

    return Jwts.builder()
        .claim(jwtClaimsProperties.getUserId(), user.getId())
        .claim(jwtClaimsProperties.getRole(), user.getRole().name())
        .claim(jwtClaimsProperties.getTokenType(), jwtProperties.getAccessTokenName())
        .setSubject(user.getUsername())
        .setExpiration(expiration)
        .setIssuedAt(now)
        .signWith(jwtProperties.getKey())
        .compact();
  }

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

  public boolean isServiceRequest(Claims claims) {
    return jwtServiceRequestProperties.getSubject().equals(claims.getSubject())
        && getUserId(claims) == jwtServiceRequestProperties.getUserId();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Claims validateRefreshTokenAndGetClaims(String token) throws JwtServiceException {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token);
      Claims claims = claimsJws.getBody();

      if (!jwtProperties
          .getRefreshTokenName()
          .equals(claims.get(jwtClaimsProperties.getTokenType()))) {
        throw new JwtServiceException(JwtServiceException.INVALID_REFRESH_TOKEN);
      }

      RefreshToken refreshTokenQueryResult = refreshTokensRepo.findById(claims.getId()).orElse(null);
      if (refreshTokenQueryResult == null) {
        throw new JwtServiceException(JwtServiceException.INVALID_REFRESH_TOKEN);
      }

      if (refreshTokenQueryResult.getUserid() != getUserId(claims)) {
        throw new JwtServiceException(JwtServiceException.INVALID_REFRESH_TOKEN);
      }

      refreshTokensRepo.deleteById(refreshTokenQueryResult.getId());
      return claims;
    }  catch (JwtException | DataAccessException ex) {
      throw new JwtServiceException(JwtServiceException.INVALID_REFRESH_TOKEN);
    }
  }

  public int getUserId(Claims claims) {
    return (Integer) claims.get(jwtClaimsProperties.getUserId());
  }

  public String getTokenType() {
    return jwtProperties.getTokenType();
  }

  public String getAccessTokenFromRequest(final HttpServletRequest request) throws JwtServiceException {
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

  public String getRole(Claims claims) {
    return (String) claims.get(jwtClaimsProperties.getRole());
  }

  @Scheduled(cron = "0 0 */12 * * *")
  protected void deleteExpiredRefreshTokens() {
    refreshTokensRepo.deleteAllExpiredTokens();
  }
}
