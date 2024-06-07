package com.microservices.auth.services;

import org.springframework.stereotype.Service;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.logging.Logger;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = Logger.getLogger(EmailService.class.getName());

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while sending Email: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while sending Email: " + e.getMessage());
        }
    }
}
