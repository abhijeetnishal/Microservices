package com.microservices.users.middlewares;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseObject {
    private Object data;
    private String message;

    public static ResponseObject getResponseObject(Object data, String message) {

        return ResponseObject.builder().data(data).message(message).build();
    }
}