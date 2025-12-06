package ss1.ong.datacenter.common.config.web;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación automática de la API
 * REST.
 *
 * Esta clase define la configuración necesaria para exponer la documentación de
 * la API utilizando Swagger UI a través de la especificación OpenAPI 3.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-05-31
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("SS1.ORG")
                        .version("1.0")
                        .description("Documentación de la API para el proyecto SS1 ORG"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public") // grupo de endpoints públicos
                .pathsToMatch("/api/dc/**") // se define el patrón de las rutas a documentar
                .build();
    }
}
