package com.example.quiz_api_management.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/* This annotation is used for creating constructor for all arguments*/
@AllArgsConstructor
/* Annotation @Data used for return json. */
@Data
public class ResponseReturn {
    // This field to check full information of time that response is returned.
    private LocalDateTime timestamp;
    private String message;
    private int statusCode;
    private boolean success;
    private Object data;
}
