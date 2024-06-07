package com.microservices.auth.config;

import com.microservices.users.UsersApplication;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final Logger logger = Logger.getLogger(UsersApplication.class.getName());

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(
                            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            return http.build();
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while configuring security filter chain: " + e.getMessage(),
                    SentryLevel.ERROR);
            logger.severe("Error occurred while configuring security filter chain: " + e.getMessage());
            return null;
        }
    }
}
