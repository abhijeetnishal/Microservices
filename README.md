# Microservices Architecture with Spring Boot

## Overview

This project is a microservices-based application built using Spring Boot. It currently includes a `User Service`, with the potential to add more services like `Post Service` and others in the future. Each service is designed to be independent, scalable, and maintainable, and communicates with others using REST APIs. We are using MySQL as the database for our services.

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
