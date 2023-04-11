package com.example.quiz_api_management.exception;

import com.example.quiz_api_management.common.ResponseReturn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/*
This handler is designed to catch any exception is found for project with Spring Boot configuration.
It increases more readability to design like this and to return specific exceptions

@ControllerAdvice is a specialization of the @Component annotation which allows to handle exceptions
across the whole application in one global handling component. It can be viewed as an interceptor of exceptions
thrown by methods annotated with @RequestMapping and similar

@ExceptionHandler is used for specific exception to custom the message and status code that we need to.

 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseReturn> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(
                new ResponseReturn(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND.value(), // No hard-code, 404
                        false, null),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ResponseReturn> handleDuplicateException(Exception exception){
        return new ResponseEntity<>(
                new ResponseReturn(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        HttpStatus.CONFLICT.value(), // 409
                        false, null),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidParams.class)
    public ResponseEntity<ResponseReturn> handleValidParamsException(Exception exception){
        return new ResponseEntity<>(
                new ResponseReturn(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(), // 400
                        false, null),
                HttpStatus.BAD_REQUEST);
    }
}
