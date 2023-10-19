package Fodong.serverdong.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;


@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi fodongApi(){
        return GroupedOpenApi.builder()
                .group("fodong-api")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Fodong Project API Document")
                .version("v0.0.1")
                .description("Fodong 프로젝트 API 명세서");

        String access = "Access Token";
        SecurityRequirement accessToken = new SecurityRequirement().addList(access);
        Components components = new Components().addSecuritySchemes(access, new SecurityScheme()
                .name(access)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization"));

        String refresh = "Refresh Token";
        SecurityRequirement refreshToken = new SecurityRequirement().addList(refresh);
        Components components1 = new Components().addSecuritySchemes(refresh, new SecurityScheme()
                .name(refresh)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Refresh"));

        return new OpenAPI()
                .components(components)
                .addSecurityItem(accessToken)
                .components(components1)
                .addSecurityItem(refreshToken)
                .info(info);
    }

}
