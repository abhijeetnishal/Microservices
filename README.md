# Microservices Architecture with Spring Boot

## Overview

This project is a microservices-based application built using Spring Boot. It currently includes a `User Service`, with the potential to add more services like `Post Service` and others in the future. Each service is designed to be independent, scalable, and maintainable, and communicates with others using REST APIs. We are using MySQL as the database for our services.

## Technologies and Frameworks Used

The project leverages several modern technologies and frameworks to ensure robustness, security, and ease of development:

- Java 21: Stable version of Java.
- MySQL: Database for storing user information.
- Maven: Build automation tool.
- Sentry: For application monitoring and error tracking.
- Spring Boot 3.3.0: Framework for building Java-based applications.
  - Spring Boot Starter Data JPA: For database access using JPA.
  - Spring Boot Starter Data Redis: For integrating with Redis.
  - Spring Boot Starter Mail: For email functionalities.
  - Spring Boot Starter OAuth2 Client: For OAuth2 authentication and authorization.
  - Spring Boot Starter Security: For securing the application.
  - Spring Boot Starter Validation: For input validation.
  - Spring Boot Starter Web: For building web, including RESTful, applications.
  - Spring Boot DevTools: For developer productivity.
- JUnit & Spring Security Test: For unit and integration testing.
- Lombok: To reduce boilerplate code.

## Features

- User Management: Create, update, delete, and retrieve user information.
- Authentication & Authorization: Secure endpoints using Spring Security and OAuth2.
- Email Notifications: Send emails for various user actions (e.g., registration, login, etc).
- Validation: Robust input validation using Spring Validation.
- Monitoring: Application monitoring and error tracking with Sentry.

## Architecture

The microservices architecture follows a modular approach where each service is independently deployable and scalable. The Users service communicates with other potential services (like Post, Comments, etc.) through RESTful APIs. Key components include:

- Controllers: Handle incoming HTTP requests and delegate to the service layer.
- Services: Contain business logic and interact with repositories.
- Repositories: Interact with the database using Spring Data JPA.
- Entities: Represent the data model for the application.
- Configuration: Externalized configuration settings for different environments.
- Utilities: Helper classes and methods to support common operations.
- Security: Configured to handle authentication and authorization.

## Configuration

- Got to src/main/resources/application.properties file and add required credentials to setup the application.

## API Endpoints for Users Service

### Create User

<details>
<summary>View Details</summary>

#### Request

- **Method**: `POST`
- **URL**: `api/v1/users`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com"
  }
  ```

#### Response

- **Status Code**: `201 Created`
- **Body**:

  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com"
  }
  ```

</details>

### Get User by ID

<details>
<summary>View Details</summary>

#### Request

- **Method**: `GET`
- **URL**: `api/v1/users/{id}`
- **Path Variable**: `id`

#### Response

- **Status Code**: `200 ok`
- **Body**:

  ```json
  {
    "id": 1,
    "userName": "John Doe",
    "email": "john.doe@example.com"
  }
  ```

</details>

### Update User by ID

<details>
<summary>View Details</summary>

#### Request

- **Method**: `PUT`
- **URL**: `api/v1/users/{id}`
- **Path Variable**: `id`
- **Headers**: `Content-Type: application/json`
- **Body**:

  ```json
  {
    "id": 1,
    "userName": "John Doe",
    "email": "john.doe@example.com"
  }
  ```

#### Response

- **Status Code**: `200 ok`
- **Body**:

  ```json
  {
    "id": 1,
    "userName": "John Doe",
    "email": "john.doe@example.com"
  }
  ```

</details>

### Delete User by ID

<details>
<summary>View Details</summary>

#### Request

- **Method**: `DELETE`
- **URL**: `api/v1/users/{id}`
- **Path Variable**: `id`

#### Response

- **Status Code**: `200 ok`

</details>

<br/>

# API Endpoints for Auth Service

## OAuth2 Sign-In

<details>
<summary>View Details</summary>

### Request

- **Method**: `GET`
- **URL**: `/v1/auth/oauth2`
- **Headers**: `Content-Type: application/json`
- **Query Parameters**:
  - `type`: The type of OAuth2 provider (e.g., `google`, `github`)
  - `code`: The authorization code from the OAuth2 provider

### Response

- **Status Code**: `200 OK`
- **Body**:
  ```json
  {
    "status": "success",
    "message": "User created",
    "data": {
      "id": 1,
      "userName": "john.doe",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "profileImage": "http://example.com/profile.jpg"
    }
  }
  ```

</details>

## Email Signup

<details>
<summary>View Details</summary>

### Request

- **Method**: `GET`
- **URL**: `/v1/auth/signup/email`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "john.doe@example.com"
  }
  ```

### Response

- **Status Code**: `200 OK`
- **Body**:
  ```json
  {
    "message": "Verification code sent",
    "data": null
  }
  ```

</details>

## Verify Email

<details>
<summary>View Details</summary>

### Request

- **Method**: `POST`
- **URL**: `/v1/auth/signup/verify`
- **Headers**: `Content-Type: application/json`
- **Query Parameters**:
  - `code`: The authorization code from the OAuth2 provider
- **Body**:
  ```json
  {
    "email": "john.doe@example.com"
  }
  ```

### Response

- **Status Code**: `200 OK`
- **Body**:
  ```json
  {
    "status": "success",
    "message": "Verification successful",
    "data": {
      "id": 1,
      "userName": "john.doe"
    }
  }
  ```

</details>
