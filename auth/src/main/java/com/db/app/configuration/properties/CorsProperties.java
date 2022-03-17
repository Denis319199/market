package com.db.app.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.cors")
@NoArgsConstructor
@Getter
@Setter
public class CorsProperties {
  private String[] origins;

  private String[] methods;

  private String[] headers;

  private String uri;
}
