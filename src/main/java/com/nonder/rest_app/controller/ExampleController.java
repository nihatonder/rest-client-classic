package com.nonder.rest_app.controller;

import com.nonder.rest_app.client.ProtectedApiClient;
import org.springframework.stereotype.Controller; // <-- Import this
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody; // <-- And this

@Controller // <-- ADD THIS ANNOTATION
@RequestMapping("/api")
public class ExampleController {

    private final ProtectedApiClient apiClient;

    public ExampleController(ProtectedApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/test")
    @ResponseBody // <-- ADD THIS ANNOTATION
    public String callProtectedService() {
        try {
            String response = apiClient.get("/secure/hello", String.class);
            return "Successfully called protected service: " + response;
        } catch (Exception e) {
            return "Error calling protected service: " + e.getMessage();
        }
    }

    @GetMapping("/hello")
    @ResponseBody // <-- ADD THIS ANNOTATION
    public String hello() {
        return "Hello from the REST client app!";
    }
}