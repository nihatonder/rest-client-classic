package com.nonder.rest_app.interceptor;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class GcpTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(GcpTokenProvider.class);

    @Value("${gcp.credentials-path:}")
    private String credentialsPath;

    @Value("${target.api.audience}")
    private String audience;

    private GoogleCredentials credentials;

    /**
     * Gets an ID token from GCP for the configured audience.
     * This token can be used to authenticate with OAuth2 protected services.
     */
    public String getIdToken() throws IOException {
        if (credentials == null) {
            credentials = loadCredentials();
        }

        IdTokenProvider idTokenProvider = (IdTokenProvider) credentials;

        // Request ID token with the target audience
        IdTokenCredentials tokenCredential = IdTokenCredentials.newBuilder()
                .setIdTokenProvider(idTokenProvider)
                .setTargetAudience(audience)
                .build();

        tokenCredential.refresh();
        String token = tokenCredential.getIdToken().getTokenValue();

        log.debug("Successfully retrieved ID token for audience: {}", audience);
        return token;
    }

    /**
     * Gets an access token from GCP.
     * Use this if your target API requires an access token instead of an ID token.
     */
    public String getAccessToken() throws IOException {
        if (credentials == null) {
            credentials = loadCredentials();
        }

        credentials.refreshIfExpired();
        String token = credentials.getAccessToken().getTokenValue();

        log.debug("Successfully retrieved access token");
        return token;
    }

    private GoogleCredentials loadCredentials() throws IOException {
        GoogleCredentials creds;

        if (credentialsPath != null && !credentialsPath.isEmpty()) {
            log.info("Loading credentials from file: {}", credentialsPath);
            try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
                creds = GoogleCredentials.fromStream(serviceAccountStream);
            }
        } else {
            log.info("Loading Application Default Credentials");
            creds = GoogleCredentials.getApplicationDefault();
        }

        // Create scoped credentials for OAuth2
        return creds.createScoped(Arrays.asList(
                "https://www.googleapis.com/auth/cloud-platform",
                "https://www.googleapis.com/auth/userinfo.email"
        ));
    }
}