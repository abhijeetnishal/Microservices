package com.microservices.auth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.auth.services.EmailService;
import com.microservices.auth.services.OAuthService;
import com.microservices.auth.services.RedisService;
import com.microservices.auth.utils.RandomCodeGenerator;
import com.microservices.auth.utils.Jwt;
import com.microservices.users.entities.Users;

import com.microservices.users.middlewares.ResponseObject;
import com.microservices.users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private RedisService redisService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private Jwt jwt;

    @GetMapping("v1/auth/oauth2")
    public ResponseEntity<Object> oAuth2SignIn(
            @RequestParam("type") String type, @RequestParam("code") String code) {
        if (!StringUtils.hasLength(type) || !StringUtils.hasLength(code)) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "All fields are required"));
        } else {
            String accessToken = oAuthService.getAccessToken(type, code);
            Object userInfo = oAuthService.getUserInfo(type, accessToken);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userInfoNode = objectMapper.valueToTree(userInfo);

            String email = userInfoNode.get("email").asText();
            Users userExists = usersService.getUserDetailsByEmail(email);

            if (!ObjectUtils.isEmpty(userExists)) {
                String token = jwt.generateToken(String.valueOf(userExists.getId()));

                return ResponseEntity.ok()
                        .body(ResponseObject.getResponseObject(token, "User with this email already exists"));
            } else {
                String firstName = "", lastName = "", userName = "", profileImage = "";

                if (type.equals("google")) {
                    firstName = userInfoNode.get("given_name").asText();
                    lastName = userInfoNode.get("family_name").asText();
                    profileImage = userInfoNode.get("picture").asText();
                    userName = email.split("@")[0];
                } else {
                    userName = userInfoNode.get("username").asText();
                    profileImage = userInfoNode.get("avatar").asText();
                }

                Users userNameExists = usersService.getUserDetailsByEmail(userName);

                // Generate random username if username already exists
                if (!ObjectUtils.isEmpty(userNameExists)) {
                    userName = RandomCodeGenerator.generateRandomUsername(userName);
                }

                Users user = new Users();
                user.setEmail(email);
                user.setUserName(userName);
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());

                Users newUser = usersService.createUser(user);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", newUser.getId());
                responseData.put("userName", userName);
                responseData.put("firstName", firstName);
                responseData.put("lastName", lastName);
                responseData.put("email", email);
                responseData.put(profileImage, profileImage);

                return ResponseEntity.ok()
                        .body(ResponseObject.getResponseObject(responseData, "User created"));
            }
        }
    }

    @PostMapping("v1/auth/signup/email")
    public ResponseEntity<Object> emailSignup(@RequestBody Users users) {

        String email = users.getEmail();

        if (!StringUtils.hasLength(email)) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "All fields are required"));
        } else {
            Users user = usersService.getUserDetailsByEmail(email);

            if (!ObjectUtils.isEmpty(user)) {
                return ResponseEntity.badRequest()
                        .body(ResponseObject.getResponseObject(null, "User already exists"));
            } else {
                String verificationCode = RandomCodeGenerator.generateVerificationCode();
                emailService.sendEmail(
                        email,
                        "Verification Code",
                        "Your Verification code is "
                                + verificationCode
                                + ". It is valid for 5 minutes only");
                redisService.setKey(email, verificationCode, 300);
                return ResponseEntity.ok()
                        .body(ResponseObject.getResponseObject(null, "Verification code sent"));
            }
        }
    }

    @PostMapping("v1/auth/email/verify")
    public ResponseEntity<Object> verifyEmail(
            @RequestParam("code") String code, @RequestBody Users users) {

        if (!StringUtils.hasLength(code)) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "Verification code is required"));
        } else if (!StringUtils.hasLength(users.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.getResponseObject(null, "All fields is required"));
        } else {
            String email = users.getEmail();
            String storedCode = redisService.getKey(email);

            if (!StringUtils.hasLength(storedCode)) {
                return ResponseEntity.badRequest()
                        .body(ResponseObject.getResponseObject(null, "Verification code expired"));
            } else {
                if (storedCode.equals(code)) {
                    String userName = email.split("@")[0];
                    Users userNameExists = usersService.getUserDetailsByUserName(userName);

                    // Generate random username if username already exists
                    if (!ObjectUtils.isEmpty(userNameExists)) {
                        userName = RandomCodeGenerator.generateRandomUsername(userName);
                    }

                    Users user = new Users();
                    user.setEmail(email);
                    user.setUserName(userName);
                    user.setCreatedAt(new Date());
                    user.setUpdatedAt(new Date());

                    Users newUser = usersService.createUser(user);
                    redisService.deleteKey(email);

                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("userName", userName);
                    responseData.put("id", newUser.getId());

                    return ResponseEntity.ok()
                            .body(ResponseObject.getResponseObject(responseData, "Verification successful"));
                } else {
                    return ResponseEntity.badRequest()
                            .body(ResponseObject.getResponseObject(null, "Invalid verification code"));
                }
            }
        }
    }
}
