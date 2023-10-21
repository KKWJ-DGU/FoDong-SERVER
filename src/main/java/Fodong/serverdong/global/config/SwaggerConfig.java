package Fodong.serverdong.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi onlyAccessTokenApi(){
        return GroupedOpenApi.builder()
                .group("AccessToken API")
                .addOpenApiCustomiser(accessNeed())
                .pathsToMatch("/**")
                .pathsToExclude("/api/member/login/**" , "/api/membertoken/reissue")
                .build();
    }
    @Bean
    public GroupedOpenApi TokenApi(){
        return GroupedOpenApi.builder()
                .group("AllToken API")
                .addOpenApiCustomiser(refreshNeed())
                .pathsToMatch("/api/membertoken/reissue")
                .build();
    }
    @Bean
    public GroupedOpenApi NonTokenApi(){
        return GroupedOpenApi.builder()
                .group("NonToken API")
                .pathsToMatch("/api/member/login/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Fodong Project API Document")
                .version("v0.0.1")
                .description("Fodong 프로젝트 API 명세서");


        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

    private SecurityScheme accessTokenSchema(){
        return new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    public OpenApiCustomiser accessNeed() {

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("AccessToken"))
                .getComponents().addSecuritySchemes("AccessToken", accessTokenSchema());
    }

    public OpenApiCustomiser refreshNeed() {
        SecurityScheme refreshTokenSchema = new SecurityScheme()
                .name("Refresh")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .addSecurityItem(new SecurityRequirement().addList("Refresh"))
                .getComponents().addSecuritySchemes("Authorization",accessTokenSchema()).addSecuritySchemes("Refresh",refreshTokenSchema);
    }
}
