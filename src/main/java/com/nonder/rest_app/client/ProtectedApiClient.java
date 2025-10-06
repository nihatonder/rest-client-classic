package com.nonder.rest_app.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class ProtectedApiClient {

    private static final Logger log = LoggerFactory.getLogger(ProtectedApiClient.class);

    private final RestClient restClient;
    private final String baseUrl;

    public ProtectedApiClient(RestClient restClient,
                             @Value("${target.api.base-url}") String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    /**
     * GET request to a protected endpoint
     */
    public <T> T get(String endpoint, Class<T> responseType) {
        String url = baseUrl + endpoint;
        log.info("Making GET request to: {}", url);

        try {
            ResponseEntity<T> responseEntity = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(responseType);

            log.info("Received response with status code: {}", responseEntity.getStatusCode());
            return responseEntity.getBody();

        } catch (Throwable t) { // <-- CHANGE from Exception to Throwable
            log.error("A fatal error occurred during REST client call to {}: {}", url, t.getMessage(), t); // Log the full stack trace
            throw new RuntimeException(t);
        }
    }
}