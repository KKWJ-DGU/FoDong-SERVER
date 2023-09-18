package Fodong.serverdong.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
