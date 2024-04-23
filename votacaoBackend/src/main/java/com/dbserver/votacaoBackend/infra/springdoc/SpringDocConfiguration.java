package com.dbserver.votacaoBackend.infra.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfiguration {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                        .bearerFormat("JWT")))
                        .info(new Info()
                                .title("Desafio Técnico Votação")
                                .description("Essa aplicação é um sistema de votação em pautas.")
                                .contact(new Contact()
                                    .name("Pedro Cecilio")
                                    .email("pedro.cecilio@dbserver.com.br"))
                        );   
                                
    }
}