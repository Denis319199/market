package com.db.app.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt.claims")
@NoArgsConstructor
@Getter
@Setter
public class JwtClaimsProperties {
  private String tokenType;

  private String userId;

  private String role;
}
