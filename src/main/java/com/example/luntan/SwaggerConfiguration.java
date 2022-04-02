
package com.example.luntan;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnProperty(prefix = "swagger",value = {"enable"},havingValue = "true")
public class SwaggerConfiguration {

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(buildApiInfo())
                .select()

                // 要扫描的API(Controller)基础包
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }


    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder().title("论坛")
                .description("论坛服务API").version("1.0").build();

    }



}
