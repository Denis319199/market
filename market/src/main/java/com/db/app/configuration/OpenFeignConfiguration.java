package com.db.app.configuration;

import com.db.app.configuration.properties.JwtProperties;
import com.db.service.JwtService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.db.client")
@RequiredArgsConstructor
public class OpenFeignConfiguration {

  private final JwtProperties jwtProperties;
  private final JwtService jwtService;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.header(
          jwtProperties.getHeader(),
          jwtProperties.getTokenType() + " " + jwtService.createServiceRequestToken());
    };
  }
}
