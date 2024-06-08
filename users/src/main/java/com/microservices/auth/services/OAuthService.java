package com.microservices.auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class OAuthService {
    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${google.token-uri}")
    private String googleTokenUri;

    @Value("${google.user-info-uri}")
    private String googleUserInfoUri;

    @Value("${discord.client-id}")
    private String discordClientId;

    @Value("${discord.client-secret}")
    private String discordClientSecret;

    @Value("${discord.redirect-uri}")
    private String discordRedirectUri;

    @Value("${discord.token-uri}")
    private String discordTokenUri;

    @Value("${discord.user-info-uri}")
    private String discordUserInfoUri;

    private final Logger logger = Logger.getLogger(OAuthService.class.getName());

    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(
            String type, String code, HttpHeaders headers) {
        try {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>() {
                {
                    put("grant_type", Collections.singletonList("authorization_code"));
                    put("code", Collections.singletonList(code));
                    put(
                            "client_id",
                            Collections.singletonList(
                                    Objects.equals(type, "google") ? googleClientId : discordClientId));
                    put(
                            "client_secret",
                            Collections.singletonList(
                                    Objects.equals(type, "google") ? googleClientSecret : discordClientSecret));
                    put(
                            "redirect_uri",
                            Collections.singletonList(
                                    Objects.equals(type, "google") ? googleRedirectUri : discordRedirectUri));
                }
            };

            return new HttpEntity<>(body, headers);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting HTTP entity: " + e.getMessage(), SentryLevel.ERROR);
            logger.severe("Error occurred while getting HTTP entity: " + e.getMessage());
            return null;
        }
    }

    public String getAccessToken(String type, String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            final HttpEntity<MultiValueMap<String, String>> request = getMultiValueMapHttpEntity(type, code, headers);

            RestTemplate restTemplate = new RestTemplate();
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    Objects.equals(type, "google") ? googleTokenUri : discordTokenUri,
                    request,
                    Map.class);

            // Extract the access token from the response
            return (String) Objects.requireNonNull(response.getBody()).get("access_token");
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting access token: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while getting access token: " + e.getMessage());
            return null;
        }
    }

    public Object getUserInfo(String type, String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    Objects.equals(type, "google") ? googleUserInfoUri : discordUserInfoUri,
                    HttpMethod.GET,
                    request,
                    Object.class);

            return response.getBody();
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting user info: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while getting user info: " + e.getMessage());
            return null;
        }
    }
}
