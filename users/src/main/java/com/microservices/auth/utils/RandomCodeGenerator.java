package com.microservices.auth.utils;

import com.microservices.users.UsersApplication;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

import java.security.SecureRandom;
import java.util.logging.Logger;

public class RandomCodeGenerator {
    private static final Logger logger = Logger.getLogger(UsersApplication.class.getName());

    public static String generateVerificationCode() {
        try {
            // Use SecureRandom for generating cryptographically secure random number
            SecureRandom random = new SecureRandom();
            StringBuilder code = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                int digit = random.nextInt(10); // Generate random digit between 0 and 9 (inclusive)
                code.append(digit);
            }

            return code.toString();
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while generating verification code: " + e.getMessage(),
                    SentryLevel.ERROR);
            logger.severe("Error occurred while generating verification code: " + e.getMessage());
            return null;
        }
    }

    public static String generateRandomUsername(String name) {
        try {
            // Generate a random number to append to the name
            SecureRandom random = new SecureRandom();
            int randomNumber = random.nextInt(1000); // Change range as needed

            // Append random number to the name to create a username
            return name.replaceAll("\\s+", "") + randomNumber;
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while generating random username: " + e.getMessage(),
                    SentryLevel.ERROR);
            logger.severe("Error occurred while generating random username: " + e.getMessage());
            return null;
        }
    }
}
