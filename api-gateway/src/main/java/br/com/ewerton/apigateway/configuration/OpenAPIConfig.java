package br.com.ewerton.apigateway.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuração do OpenAPI para a API Gateway.
 * Esta classe configura o Swagger UI para exibir a documentação das APIs de forma agrupada,
 * com base nas rotas configuradas na API Gateway.
 * A configuração é aplicada apenas para os serviços que possuem o sufixo "-service" no nome da rota.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configura os grupos de APIs no Swagger UI com base nas rotas definidas no gateway.
     * A configuração cria um grupo de APIs para cada rota que possui o sufixo "-service".
     * A cada rota que atende a esse critério, é gerado um novo grupo na documentação Swagger,
     * exibindo suas rotas específicas.
     *
     * @param configParameters Parâmetros de configuração do Swagger UI.
     * @param locator          Localizador das definições de rota na API Gateway.
     * @return Lista de grupos de APIs configuradas para o Swagger UI. (Retorna uma lista vazia após a configuração)
     */
    @Bean
    @Lazy(value = false)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters configParameters, RouteDefinitionLocator locator) {
        // Obtém as definições de rotas da API Gateway
        var definitions = locator.getRouteDefinitions().collectList().block();

        // Filtra as rotas que possuem o sufixo "-service" no nome e cria grupos no Swagger UI
        definitions.stream()
                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                .forEach(routeDefinition -> {
                    String name = routeDefinition.getId();  // Nome da rota
                    configParameters.addGroup(name);  // Adiciona o grupo no Swagger UI
                    GroupedOpenApi.builder()  // Configura o OpenAPI para a rota
                            .pathsToMatch("/" + name + "/**")  // Define os caminhos que devem ser documentados
                            .group(name)  // Associa o grupo à rota
                            .build();
                });

        // Retorna uma lista vazia (os grupos já foram adicionados via SwaggerUiConfigParameters)
        return new ArrayList<>();
    }
}
