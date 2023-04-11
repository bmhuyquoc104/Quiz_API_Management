package com.example.quiz_api_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotValidParams extends IllegalArgumentException{
    public NotValidParams(String message){
        super(message);
    }
}
