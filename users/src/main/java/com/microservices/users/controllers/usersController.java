package com.microservices.users.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.users.entities.Users;
import com.microservices.users.middlewares.ResponseObject;
import com.microservices.users.services.UsersService;

@RestController
public class usersController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/api/v1/users")
    public ResponseEntity<Object> getUser(@RequestParam String id) {
        Users user = usersService.getUserDetailsById(UUID.fromString(id));

        if (ObjectUtils.isEmpty(user)) {
            return ResponseEntity.badRequest().body(ResponseObject.getResponseObject(null, "No user found"));
        } else {
            return ResponseEntity.ok().body(ResponseObject.getResponseObject(user, "User details"));
        }
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<Object> createUser(@RequestBody Users user) {
        String email = user.getEmail();

        if (!StringUtils.hasLength(email)) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "User details required"));
        } else {
            Users existingUser = usersService.getUserDetailsByEmail(email);

            if (!ObjectUtils.isEmpty(existingUser)) {
                return ResponseEntity.badRequest()
                        .body(ResponseObject.getResponseObject(null, "User already exists"));
            } else {
                Users newUser = usersService.createUser(user);

                return ResponseEntity.ok().body(ResponseObject.getResponseObject(newUser, "User created"));
            }
        }
    }

    @PutMapping("v1/api/users/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable String id, @RequestBody Users user) {
        Users userExists = usersService.getUserDetailsById(UUID.fromString(id));

        if (ObjectUtils.isEmpty(userExists)) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "User not found"));
        } else {
            usersService.updateUserById(user, userExists);
            return ResponseEntity.ok().body(ResponseObject.getResponseObject(null, "User data updated"));
        }
    }

    @DeleteMapping("v1/api/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        Users userExists = usersService.getUserDetailsById(UUID.fromString(id));

        if (ObjectUtils.isEmpty(userExists)) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "User not found"));
        } else {
            usersService.deleteUserById(UUID.fromString(id));
            return ResponseEntity.ok().body(ResponseObject.getResponseObject(null, "User deleted"));
        }
    }
}
