package self.edu.shopbiz.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mpjoshi on 11/15/19.
 */

@Configuration
public class CustomOpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info( new Info()
                        .title("Shopbiz API Reference")
                        .version("1.0.0")
                        .description("App to demonstrate online shopping functionality")
                        .contact(DEFAULT_CONTACT)
                        .license(license));

    }

    public static final Contact DEFAULT_CONTACT = new Contact()
                                                        .email("test@test.com")
                                                        .name(" M. P.")
                                                        .url("http://www.shopbiz.self.edu");

    public static final License license = new License()
                                                .name("MIT")
                                                .url("https://mit-license.org");


//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("self.edu.shopbiz.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
//                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
//    }

}
