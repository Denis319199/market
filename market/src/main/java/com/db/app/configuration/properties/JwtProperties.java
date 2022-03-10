package com.db.app.configuration.properties;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
@NoArgsConstructor
@Getter
@Setter
public class JwtProperties {

    private String secret;

    private String tokenType;

    private String header;

    private String accessTokenName;

    private SecretKey key;


    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
