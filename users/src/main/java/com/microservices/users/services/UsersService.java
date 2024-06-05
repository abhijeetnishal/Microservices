package com.microservices.users.services;

import java.util.UUID;

import com.microservices.users.entities.Users;

public interface UsersService {
    Users createUser(Users user);

    Users getUserDetailsById(UUID id);

    Users getUserDetailsByEmail(String email);

    void updateUserById(Users updatedUsers, Users existingUsers);

    void deleteUserById(UUID id);
}
