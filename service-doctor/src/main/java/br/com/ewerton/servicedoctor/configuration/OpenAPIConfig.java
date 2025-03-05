package br.com.ewerton.servicedoctor.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação OpenAPI para a API de serviço de médicos.
 * Este componente configura a documentação da API usando a biblioteca Swagger (OpenAPI v3).
 * <p>
 * A documentação gerada será acessível por meio de um ponto final, geralmente
 * disponível na URL "/swagger-ui.html", onde os detalhes sobre os endpoints da API
 * poderão ser visualizados.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Doctor Service API", version = "v1", description = "Documentation of Doctor Service"))
public class OpenAPIConfig {

    /**
     * Cria e configura o objeto OpenAPI para personalizar a documentação da API.
     * Aqui, é definida a licença e informações adicionais sobre a API.
     *
     * @return Um objeto {@link OpenAPI} configurado com as informações da API e componentes.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())  // Adiciona os componentes da API (ex: schemas, security, etc)
                .info(new io.swagger.v3.oas.models.info.Info()  // Define as informações da API
                        .title("Doctor Service API")  // Título da API
                        .version("v1")  // Versão da API
                        .license(new License()  // Define a licença da API
                                .name("Ewerton Rodrigues 1.0")
                                .url("http://www.ewerton.com.br")));
    }
}
