package org.mlgnu.learnova;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "Learnova API",
                        version = "1.0.0",
                        description = "API documentation for Learnova application"),
        servers = @Server(url = "http://localhost:8080/api", description = "Local"))
@SecurityScheme(
        name = "bearerJWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Bearer token authentication")
@SpringBootApplication
public class LearnovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnovaApplication.class, args);
    }
}
