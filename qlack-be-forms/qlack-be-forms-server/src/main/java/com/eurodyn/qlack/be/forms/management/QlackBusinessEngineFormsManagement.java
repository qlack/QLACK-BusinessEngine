package com.eurodyn.qlack.be.forms.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The main class of the Spring Boot application.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@SpringBootApplication
@EnableJpaRepositories({
    "com.eurodyn.qlack.be.forms.management.repository"
})
@EntityScan({
    "com.eurodyn.qlack.be.common.model",
    "com.eurodyn.qlack.fuse.lexicon.model",
    "com.eurodyn.qlack.fuse.aaa.model",
    "com.eurodyn.qlack.be.forms.management.model"
})
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.be.forms.management.controller",
    "com.eurodyn.qlack.be.forms.management.service",
    "com.eurodyn.qlack.be.forms.management.mapper",
    "com.eurodyn.qlack.util"
})
public class QlackBusinessEngineFormsManagement {

  public static void main(String[] args) {
    SpringApplication.run(QlackBusinessEngineFormsManagement.class, args);
  }

  @Bean
  public WebXmlBridge webXmlBridge() {
    return new WebXmlBridge();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8081")
            .allowedHeaders("*");
      }
    };
  }

}