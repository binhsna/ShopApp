package com.binhnc.shopapp.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce API",
                version = "1.0.0",
                description = "Ứng dụng bán hàng"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server"),
                @Server(url = "http://127.0.0.1:8080", description = "Production Server")
        }
)
@SecurityScheme(
        name = "bearer-key", // Biến lưu token -> @SecurityRequirement
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
// http://localhost:8080/swagger-ui/index.html
@Configuration
public class OpenApiConfig {

}