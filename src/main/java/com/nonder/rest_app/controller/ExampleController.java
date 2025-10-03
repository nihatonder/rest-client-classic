package com.nonder.rest_app.controller;

import com.nonder.rest_app.client.ProtectedApiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExampleController {

    private final ProtectedApiClient apiClient;

    public ExampleController(ProtectedApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Public endpoint (no OAuth2 required) that calls the protected service
     */
    @GetMapping("/test")
    public String callProtectedService() {
        try {
            String response = apiClient.get("/secure/hello", String.class);
            return "Successfully called protected service: " + response;
        } catch (Exception e) {
            return "Error calling protected service: " + e.getMessage();
        }
    }

    /**
     * Public endpoint (no OAuth2 required) - your own service
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello from the REST client app!";
    }
}