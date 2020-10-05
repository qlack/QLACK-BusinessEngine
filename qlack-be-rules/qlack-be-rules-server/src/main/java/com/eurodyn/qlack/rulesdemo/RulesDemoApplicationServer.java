package com.eurodyn.qlack.rulesdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({
        "com.eurodyn.qlack.rulesdemo.repository"
})
@EntityScan({
        "com.eurodyn.qlack.rulesdemo.model"
})
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.rulesdemo.web",
        "com.eurodyn.qlack.rulesdemo.service",
        "com.eurodyn.qlack.rulesdemo.mapper",
        "com.eurodyn.qlack.util.*"
})
public class RulesDemoApplicationServer {

  public static void main(String[] args) {
    SpringApplication.run(RulesDemoApplicationServer.class, args);
  }

}
