package com.flexksx.spring_rabbitmq_test.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class RaftLeaderConfig {
    @Value("${onedollarbid.protocol}")
    private String protocol;

    @Value("${onedollarbid.host}")
    private String host;

    @Value("${onedollarbid.port}")
    private int port;

    @Value("${onedollarbid.node}")
    private int node;

    private String baseUrl;

    @PostConstruct
    public void init() {
        updateBaseUrl();
    }

    public void updateBaseUrl() {
        this.baseUrl = protocol + "://" + host + ":" + port;
    }
}
