package mycode.online_shop_api.app.system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Online Shop API", version = "v1"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI onlineShopOpenAPI() {
        var info = new io.swagger.v3.oas.models.info.Info()
                .title("Online Shop API")
                .version("v1")
                .description("REST API powering the Online Shop dashboard and storefront. " +
                        "All secured endpoints require a JWT token obtained from /api/v1/users/login or register.")
                .contact(new Contact()
                        .name("MyCodeSchool Team")
                        .email("support@mycodeschool.com")
                        .url("https://github.com/mycodeschool/online_shop_api"))
                .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local development"),
                        new Server().url("https://api.mycodeschool.shop").description("Production")
                ))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name("bearerAuth")
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"));
    }
}
