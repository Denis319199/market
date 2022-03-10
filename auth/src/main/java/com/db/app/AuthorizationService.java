package com.db.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.db")
@ConfigurationPropertiesScan(basePackages = "com.db.app.configuration.properties")
@EnableScheduling
public class AuthorizationService {
  public static void main(String[] args) {
    SpringApplication.run(AuthorizationService.class, args);
  }
}
