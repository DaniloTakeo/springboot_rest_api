package com.example.clinicapi.infra.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Classe de configuração para a documentação OpenAPI (Swagger)
 * da API da Clínica.
 * Define as informações básicas da API
 * que serão exibidas na interface Swagger UI.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configura e retorna um objeto OpenAPI com as informações da API.
     * Inclui título, versão e descrição da API.
     *
     * @return Um objeto OpenAPI configurado.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Minha API Clínica")
                .version("v1")
                .description("Documentação da API para gerenciamento de "
                        + "pacientes, médicos e consultas."));
    }
}
