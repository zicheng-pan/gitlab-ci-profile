package com.example.demoforprofile;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("hivemq")
public class HivemqConfig {
    private boolean eseEnabled;
}
