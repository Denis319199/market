package com.db.service;

import com.db.app.configuration.properties.JwtClaimsProperties;
import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.JwtServiceRequestProperties;
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
  public String createRefreshToken(User user) {
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
      return null;
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

  public boolean isServiceRequest(Claims claims) {
    return jwtServiceRequestProperties.getSubject().equals(claims.getSubject())
        && getUserId(claims) == jwtServiceRequestProperties.getUserId();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Claims validateRefreshTokenAndGetClaims(String token) {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token);
      Claims claims = claimsJws.getBody();

      if (!jwtProperties
          .getRefreshTokenName()
          .equals(claims.get(jwtClaimsProperties.getTokenType()))) {
        return null;
      }

      Optional<RefreshToken> refreshTokenQueryResult = refreshTokensRepo.findById(claims.getId());
      if (refreshTokenQueryResult.isEmpty()) {
        return null;
      }

      if (refreshTokenQueryResult.get().getUserid() != getUserId(claims)) {
        return null;
      }

      refreshTokensRepo.deleteById(refreshTokenQueryResult.get().getId());
      return claims;
    } catch (Exception ex) {
      return null;
    }
  }

  public int getUserId(Claims claims) {
    return (Integer) claims.get(jwtClaimsProperties.getUserId());
  }

  public String getTokenType() {
    return jwtProperties.getTokenType();
  }

  public String getAccessTokenFromRequest(HttpServletRequest request) {
    String headerValue = request.getHeader(jwtProperties.getHeader());
    if (Objects.isNull(headerValue)) {
      return null;
    }

    return headerValue.replaceFirst(jwtProperties.getTokenType() + " ", "");
  }

  @Scheduled(cron = "0 0 */12 * * *")
  protected void deleteExpiredRefreshTokens() {
    refreshTokensRepo.deleteAllExpiredTokens();
  }
}
