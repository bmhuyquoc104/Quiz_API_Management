package com.example.quiz_api_management.common;

import lombok.AllArgsConstructor;

/* This annotation is used for creating constructor for all arguments*/
@AllArgsConstructor
public class ResponseReturn {
    private String responseMessage;
    private int httpStatus;
    private boolean isSuccess;
    private Object data;
}
