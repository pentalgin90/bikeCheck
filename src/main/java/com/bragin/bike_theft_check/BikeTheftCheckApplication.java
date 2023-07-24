package com.bragin.bike_theft_check;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BikeTheftCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeTheftCheckApplication.class, args);
    }
}
