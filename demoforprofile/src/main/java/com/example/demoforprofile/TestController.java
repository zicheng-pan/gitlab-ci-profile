package com.example.demoforprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {
    @Value("${testdata.key}")
    String key;

    @Value("${testdata.value}")
    String value;

    @Autowired
    HivemqConfig hivemqConfig;

    @RequestMapping("/test")
    public String getTestData() {
        return key + " " + value+" hivemq config:"+hivemqConfig.isEseEnabled();
    }
}
