package com.pge.booklyzer.shared.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI myOpenAPI() {

   License license = new License().name("License").url("");

    Info info = new Info()
        .title("Booklyzer API")
        .version("1.0")
        .description("API.").termsOfService("")
        .license(license);

    return new OpenAPI().info(info).addSecurityItem(new SecurityRequirement().
                    addList("Bearer Authentication"))
            .components(new Components().addSecuritySchemes
                    ("Bearer Authentication", createAPIKeyScheme()));
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
  }
}