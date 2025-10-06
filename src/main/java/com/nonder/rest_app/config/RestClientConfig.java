package com.nonder.rest_app.config;

import com.nonder.rest_app.interceptor.OAuth2ClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

public class RestClientConfig {

    private final OAuth2ClientInterceptor oauth2Interceptor;

    public RestClientConfig(OAuth2ClientInterceptor oauth2Interceptor) {
        this.oauth2Interceptor = oauth2Interceptor;
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestInterceptor(oauth2Interceptor)
                .build();
    }
}