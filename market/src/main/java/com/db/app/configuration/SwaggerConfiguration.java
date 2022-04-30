package com.db.app.configuration;

import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {
  private final SwaggerProperties swaggerProperties;
  private final JwtProperties jwtProperties;

  private static final String API_TOKEN = "Token";

  @Bean
  public OpenAPI api() {
    return new OpenAPI()
            .info(
                    new Info()
                            .description(swaggerProperties.getDescription())
                            .title(swaggerProperties.getTitle()))
            .addSecurityItem(new SecurityRequirement().addList(API_TOKEN))
            .components(new Components().addSecuritySchemes(API_TOKEN, securityScheme()));
  }

  private SecurityScheme securityScheme() {
    return new SecurityScheme()
            .in(SecurityScheme.In.HEADER)
            .scheme(API_TOKEN)
            .name(jwtProperties.getHeader())
            .type(SecurityScheme.Type.APIKEY);
  }

  @Bean
  public GroupedOpenApi groupedOpenApi() {
    return GroupedOpenApi.builder()
            .packagesToScan(swaggerProperties.getBasePackage())
            .group(swaggerProperties.getVersion())
            .build();
  }
}
