package com.pineapple.springjpa.infrastructure.configuration;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

  @Value("${swagger.api-info.title}")
  private String title;

  @Value("${swagger.api-info.description}")
  private String description;

  @Value("${swagger.api-info.version}")
  private String version;

  @Value("${swagger.api-info.termsOfService}")
  private String termsOfService;

  @Value("${swagger.api-info.contact.name}")
  private String teamName;

  @Value("${swagger.api-info.contact.url-slack}")
  private String contactURL;

  @Value("${swagger.api-info.contact.email}")
  private String contactEmail;

  @Bean
  public Docket documentation() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.pineapple.springjpa"))
        .build()
        .pathMapping("/")
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        title,
        description,
        version,
        termsOfService,
        new Contact(teamName, contactURL, contactEmail),
        null,
        null,
        Collections.emptyList());
  }
}
