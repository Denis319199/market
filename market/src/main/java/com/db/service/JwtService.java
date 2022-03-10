package com.db.service;

import com.db.app.configuration.properties.JwtProperties;
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
@Validated
public class JwtService {
    private final JwtProperties jwtProperties;

    private final String TOKEN_TYPE_CLAIM = "token_type";
    private final String USER_ID_CLAIM = "user_id";
    private final String ROLE_CLAIM = "role";

    public Claims validateAccessTokenAndGetClaims(@NotNull String token) {
        try {
            Jws<Claims> claimsJws =
                    Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            if (jwtProperties.getAccessTokenName().equals(claims.get(TOKEN_TYPE_CLAIM))) {
                return claims;
            }
            return null;
        } catch (JwtException ex) {
            return null;
        }
    }

    public int getUserId(Claims claims) {
        return (Integer) claims.get(USER_ID_CLAIM);
    }

    public String getRole(Claims claims) {
        return (String) claims.get(ROLE_CLAIM);
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader(jwtProperties.getHeader());
        if (Objects.isNull(headerValue)) {
            return null;
        }

        return headerValue.replaceFirst(jwtProperties.getTokenType() + " ", "");
    }
}