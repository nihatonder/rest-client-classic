package com.nonder.rest_app.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class OAuth2ClientInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ClientInterceptor.class);

    private final GcpTokenProvider tokenProvider;

    public OAuth2ClientInterceptor(GcpTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                       ClientHttpRequestExecution execution) throws IOException {
        // Get the ID token from GCP
        String token = tokenProvider.getIdToken();

        // Add the Bearer token to the Authorization header
        request.getHeaders().setBearerAuth(token);

        log.debug("Added Authorization header to request: {} {}",
                 request.getMethod(), request.getURI());

        return execution.execute(request, body);
    }
}