# Microservices Architecture with Spring Boot

## Overview

This project is a microservices-based application built using Spring Boot. It currently includes a `User Service`, with the potential to add more services like `Post Service` and others in the future. Each service is designed to be independent, scalable, and maintainable, and communicates with others using REST APIs. We are using MySQL as the database for our services.

## Technologies and Frameworks Used

The project leverages several modern technologies and frameworks to ensure robustness, security, and ease of development:

- Java 21: The latest stable version of Java.
- MySQL: Database for storing user information.
- Sentry: For application monitoring and error tracking.
- Spring Boot 3.3.0: Framework for building Java-based applications.
  - Spring Boot Starter Data JPA: For database access using JPA.
  - Spring Boot Starter Data Redis: For integrating with Redis.
  - Spring Boot Starter Mail: For email functionalities.
  - Spring Boot Starter OAuth2 Client: For OAuth2 authentication and authorization.
  - Spring Boot Starter Security: For securing the application.
  - Spring Boot Starter Validation: For validating REST endpoints.
  - Spring Boot Starter Web: For building web, including RESTful, applications.
  - Spring Boot DevTools: For developer productivity.
- JUnit & Spring Security Test: For unit and integration testing.
- Lombok: To reduce boilerplate code.

## Configuration

Configuration file code in application.properties

```bash
# MySQL configuration
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# Hibernate
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# Sentry configuration
sentry.dsn=
# Set traces_sample_rate to 1.0 to capture 100%
# of transactions for performance monitoring.
# We recommend adjusting this value in production.
sentry.traces-sample-rate=1.0
```

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
