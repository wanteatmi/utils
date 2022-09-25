package com.dog.tipspushutil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TipsPushUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(TipsPushUtilApplication.class, args);
    }

}
