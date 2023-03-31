package com.example.quiz_api_management.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
This exception is used to catch if entity is not found.
*/

@ResponseStatus(HttpStatus.NOT_FOUND)
    public class NotFoundException extends EntityNotFoundException {
    public NotFoundException(String message) {
        super(message);
    }
}
