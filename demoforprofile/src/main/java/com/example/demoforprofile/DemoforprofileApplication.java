package com.example.demoforprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({HivemqConfig.class})
public class DemoforprofileApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoforprofileApplication.class, args);
    }

}
