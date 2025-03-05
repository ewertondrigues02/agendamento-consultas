package br.com.ewerton.serviceschedules.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

/**
 * Configuração do Swagger para a documentação da API de Agendamentos.
 * <p>
 * Esta classe configura o Swagger para fornecer a documentação da API,
 * incluindo detalhes sobre o título, versão e licença da API.
 * <p>
 * A documentação gerada é acessível através do Swagger UI.
 */
@OpenAPIDefinition(info = @Info(title = "Schedules Service API", version = "v1", description = "Documentation of Schedules Service"))
public class OpenAPIConfig {

    /**
     * Configura a documentação da API personalizada com informações adicionais,
     * como o título, versão e licença.
     *
     * @return A configuração personalizada do Swagger {@link OpenAPI}.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Schedules Service API")
                        .version("v1")
                        .license(new License()
                                .name("Ewerton Rodrigues 1.0")
                                .url("http://www.ewerton.com.br")));
    }
}
