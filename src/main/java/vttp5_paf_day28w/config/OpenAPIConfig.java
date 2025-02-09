package vttp5_paf_day28w.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean 
    public OpenAPI openAPI() {

        return new OpenAPI().info(
            new Info()
            .title("PAF Day 18 Workshop")
            .description("Testing API using OpenAPI public interface")
            .version("1.0")
        );

    }
    
}
