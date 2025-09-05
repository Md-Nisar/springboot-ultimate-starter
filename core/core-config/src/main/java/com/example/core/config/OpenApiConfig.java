package com.example.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/** Configuration class for OpenAPI documentation.
 * Sets up the OpenAPI bean with API information and server details.
 * The Swagger UI page will be available at <a href="http://server:port/context-path/swagger-ui.html">...</a>
 * and the OpenAPI description will be available at the following url for JSON format: <a href="http://server:port/context-path/v3/api-docs">...</a>
 */
@Configuration
public class OpenApiConfig {

    private static final Logger log = LoggerFactory.getLogger(OpenApiConfig.class);
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String X_AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";

    @Value("${app.api.doc.url.dev:}")
    private String devUrl;

    @Value("${app.api.doc.url.prod:}")
    private String prodUrl;

    @Bean
    public OpenAPI openApiDefinition() {
        log.info("Initializing OpenAPI documentation configuration");
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocs())
                .servers(servers())
                .addSecurityItem(securityRequirement())
                .components(components());
    }

    private Info info() {
        return new Info()
                .title("Ultimate APIs")
                .version("1.0.0")
                .description("API documentation for the core enterprise application resources.")
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                .contact(new Contact().name("Support Team").email("support@example.com"))
                .termsOfService("https://example.com");

    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .url("https://docs.example.com/api")
                .description("API Guidelines");
    }

    private List<Server> servers() {
        List<Server> servers = new ArrayList<>();
        if (!devUrl.isBlank()) {
            servers.add(new Server().url(devUrl).description("Development Server"));
        }
        if (!prodUrl.isBlank()) {
            servers.add(new Server().url(prodUrl).description("Production Server"));
        }
        return servers;
    }


    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(AUTHORIZATION_HEADER);
    }

    private Components components() {
        SecurityScheme authorization = new SecurityScheme()
                .name(AUTHORIZATION_HEADER)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Please enter a valid JWT in the format 'Bearer [token]'");

        SecurityScheme xAAuthToken = new SecurityScheme()
                .name(X_AUTH_TOKEN_HEADER)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .description("Please enter a valid JWT in the format 'X-AUTH-TOKEN [token]'");

        return new Components()
                .addSecuritySchemes(AUTHORIZATION_HEADER, authorization)
                .addSecuritySchemes(X_AUTH_TOKEN_HEADER, xAAuthToken);

    }

    @Bean
    public GroupedOpenApi internalApi() {
        log.info("Registering OpenAPI group: internal");
        return GroupedOpenApi.builder()
                .group("internal")
                .pathsToMatch("v1/internal/**")
                .pathsToExclude("v1/internal/health/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        log.info("Registering OpenAPI group: admin");
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/v1/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        log.info("Registering OpenAPI group: user");
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        log.info("Registering OpenAPI group: public");
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/v1/public/**")
                .build();
    }

}