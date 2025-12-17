package com.apiRest.VUTTR.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-key"))
                .info(new Info()
                        .title("VUTTR API")
                        .version("v1")
                        .description("VUTTR (Very Useful Tools to Remember) is a simple repository for managing tools along with their names, links, descriptions, and tags. Authentication is handled via JWT.")
                        .contact(new Contact()
                                .name("Gabriel Meneghini")
                                .email("gabriel.meneghini.francelino@gmail.com")));
    }

}
