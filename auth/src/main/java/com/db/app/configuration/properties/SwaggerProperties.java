package com.db.app.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
@NoArgsConstructor
@Getter
@Setter
public class SwaggerProperties {
    private String title;

    private String description;

    private String version;

    private String basePackage;

}
