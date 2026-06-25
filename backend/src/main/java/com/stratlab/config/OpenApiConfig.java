package com.stratlab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stratLabOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StratLab API")
                        .description("StratLab - Valorant Team Analytics Platform\n\n"
                                + "REST API for managing teams, players, matches, "
                                + "and performance analytics.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Alfredo Méndez Llaupe")
                                .url("https://github.com/amendezllaupe"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
