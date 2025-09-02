package com.example.core.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/** Configuration class for OpenAPI documentation.
 * Sets up the OpenAPI bean with API information and server details.
 * The Swagger UI page will be available at <a href="http://server:port/context-path/swagger-ui.html">...</a>
 * and the OpenAPI description will be available at the following url for JSON format: <a href="http://server:port/context-path/v3/api-docs">...</a>
 */
@Configuration
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Token-based authentication for securing API endpoints. Please enter a valid JWT in the format 'Bearer [token]'."
)
public class OpenApiConfig {

    @Value("${app.api.doc.url.dev}")
    private String devUrl;

    @Value("${app.api.doc.url.prod}")
    private String prodUrl;

    @Bean
    public OpenAPI openApiDefinition() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development Server");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Production Server");

        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise API")
                        .version("1.0.0")
                        .description("API documentation for the core enterprise application. This API provides essential services for managing tenants and other core resources.")
                        .termsOfService("https://example.com")
                        .contact(new Contact().name("Support Team").email("support@example.com"))
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .servers(List.of(devServer, prodServer));
    }

}