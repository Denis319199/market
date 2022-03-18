package com.db.app.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.unsecured")
@NoArgsConstructor
@Getter
@Setter
public class UnsecuredEndpointsProperties {
  private String[] endpoints;

  private String[] getMethodEndpoints;

  private String[] swaggerPaths;
}
