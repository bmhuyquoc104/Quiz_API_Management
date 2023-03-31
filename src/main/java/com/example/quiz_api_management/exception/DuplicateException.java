package com.example.quiz_api_management.exception;

import jakarta.persistence.NonUniqueResultException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
This exception is used to catch duplicate field of an instance of Entity found.
 */

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateException extends NonUniqueResultException {
    public DuplicateException(String message) {
        super(message);
    }
}