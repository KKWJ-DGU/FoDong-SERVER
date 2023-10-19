package Fodong.serverdong.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.json.HTTP;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpHeaders;
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
        String refresh = "Refresh Token";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(access)
                .addList(refresh);

        SecurityScheme accessTokenSchema = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");


        SecurityScheme refreshTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Refresh");

        Components components = new Components()
                .addSecuritySchemes(access,accessTokenSchema)
                .addSecuritySchemes(refresh,refreshTokenScheme);


        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(info);
    }

}
