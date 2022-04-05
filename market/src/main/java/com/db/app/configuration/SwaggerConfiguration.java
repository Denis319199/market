package com.db.app.configuration;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .securityContexts(securityContexts())
        .securitySchemes(Collections.singletonList(apiKey()));
  }

  private ApiKey apiKey() {
    return new ApiKey("ACCESS_TOKEN", "Authorization", "header");
  }

  private List<SecurityReference> auth() {
    AuthorizationScope[] authorizationScopes =
        new AuthorizationScope[] {new AuthorizationScope("global", "access token")};

    return Collections.singletonList(new SecurityReference("ACCESS_TOKEN", authorizationScopes));
  }

  private List<SecurityContext> securityContexts() {
    return Collections.singletonList(SecurityContext.builder().securityReferences(auth()).build());
  }
}
