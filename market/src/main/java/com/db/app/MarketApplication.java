package com.db.app;

import com.db.model.dto.filter.FilterTest;
import com.db.model.dto.filter.FilterTestTest;
import com.db.utility.filter.Filter;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.db")
@ConfigurationPropertiesScan(basePackages = "com.db.app.configuration.properties")
public class MarketApplication {

  public static void main(String[] args) {
    SpringApplication.run(MarketApplication.class, args);
  }
}
