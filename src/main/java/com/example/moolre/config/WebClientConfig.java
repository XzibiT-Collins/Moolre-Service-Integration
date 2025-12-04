package com.example.moolre.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${moolre.api.base-url}")
    private String moolreApiBaseUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder
                .baseUrl(moolreApiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
