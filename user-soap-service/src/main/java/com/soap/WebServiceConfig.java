package com.soap;

import jakarta.xml.ws.Endpoint;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class WebServiceConfig {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @PostConstruct
    public void publishEndpoint() {
        Endpoint.publish("http://localhost:9002/auth", authServiceImpl);
    }
}