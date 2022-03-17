package com.db.app.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt.service-request")
@NoArgsConstructor
@Getter
@Setter
public class JwtServiceRequestProperties {
  private String subject;

  private Integer userId;
}
