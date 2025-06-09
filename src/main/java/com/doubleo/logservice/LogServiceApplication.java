package com.doubleo.logservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogServiceApplication.class, args);
    }
}
