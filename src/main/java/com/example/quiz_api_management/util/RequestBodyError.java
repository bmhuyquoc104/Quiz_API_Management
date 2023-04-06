package com.example.quiz_api_management.util;

import com.example.quiz_api_management.common.RequiredFieldSignal;
import com.example.quiz_api_management.common.ResponseReturn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestBodyError {
    public ResponseEntity<ResponseReturn> returnRequiredFields(BindingResult bindingResult){
        List<RequiredFieldSignal> requiredFieldSignals = new ArrayList<>();
        String errorMessage = "";
        bindingResult.getFieldErrors().forEach(
                fieldError -> {
                    requiredFieldSignals.add(
                            new RequiredFieldSignal(fieldError.getField(), fieldError.getDefaultMessage()));
                }
        );
        for (RequiredFieldSignal requiredFieldSignal : requiredFieldSignals){
            errorMessage += "Field required: "+ requiredFieldSignal.getField() +
                    ", cause: "+ requiredFieldSignal.getCause() + " \n";
        }
        return new ResponseEntity<>(new ResponseReturn(LocalDateTime.now(),
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                false,
                null), HttpStatus.BAD_REQUEST);
    }
}
