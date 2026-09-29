package com.shop1c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Shop1CApp {

    public static void main(String[] args) {
        SpringApplication.run(Shop1CApp.class, args);
    }
}
