package com.microservices.users.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservices.users.entities.Users;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    Users findByUserName(String userName);

    Users findByEmail(String email);
}
