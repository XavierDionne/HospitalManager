/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class SwaggerConfig
{
    /**
     * @return The {@link Docket} object for swagger 2 support. This cannot be null. 
     */
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .apiInfo(getApiInfo());
    }

    /**
     * @return The {@link ApiInfo} containing the general api details for this
     *         service. This cannot be null.
     */
    private ApiInfo getApiInfo()
    {
        final Contact contact = new Contact(
            "Xavier Dionne",
            null,
            "xavierdionne9@gmail.com");

        return new ApiInfoBuilder()
            .title("Hospital Manager")
            .description("A REST API to manage hospital data.")
            .version("1.0.0")
            .contact(contact)
            .build();
    } 
}