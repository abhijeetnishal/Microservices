package com.microservices.users.services;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.users.entities.Users;
import com.microservices.users.repositories.UsersRepository;
import com.microservices.users.utils.UsersServiceUtils;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;

    private final Logger logger = Logger.getLogger(UsersServiceImpl.class.getName());

    @Override
    public Users createUser(Users user) {
        try {
            return usersRepository.save(user);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while creating user: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while creating user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Users getUserDetailsById(UUID id) {
        try {
            return usersRepository.findById(id).orElse(null);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting user by id: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while getting user by id: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Users getUserDetailsByEmail(String email) {
        try {
            return usersRepository.findByEmail(email);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting user by email: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while getting user by email: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Users getUserDetailsByUserName(String userName) {
        try {
            return usersRepository.findByUserName(userName);
        } catch (Exception e) {
            logger.severe("Error occurred while getting user by username: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateUserById(Users updatedUser, Users existingUser) {
        try {
            // Copy non-null properties from updatedUser to existingUser
            BeanUtils.copyProperties(
                    updatedUser,
                    existingUser,
                    Objects.requireNonNull(UsersServiceUtils.getNullPropertyNames(updatedUser)));

            usersRepository.save(existingUser);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while updating user: " + e.getMessage(), SentryLevel.ERROR);
            logger.severe("Error occurred while updating user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUserById(UUID id) {
        try {
            usersRepository.deleteById(id);
        } catch (Exception e) {
            logger.severe("Error occurred while deleting user: " + e.getMessage());
        }
    }
}
