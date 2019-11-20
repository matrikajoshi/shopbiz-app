package self.edu.shopbiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mpjoshi on 11/15/19.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final Contact DEFAULT_CONTACT = new Contact(
            " M. P.", "http://www.self.edu.shopbiz", "test@test.com");

    ApiInfo apiInfo() {
        return  new ApiInfoBuilder()
                .title("Shopbiz API Reference")
                .version("1.0.0")
                .description("App to demonstrate online shopping functionality")
                .contact(DEFAULT_CONTACT)
                .license("MIT")
                .licenseUrl("https://mit-license.org")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "", "header");             // <<< === Create a Heaader (We are createing header named "Authorization" here)
    }

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<String>(Arrays.asList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("self.edu.shopbiz.controller"))
                .paths(PathSelectors.any())
                .build()
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }

}
