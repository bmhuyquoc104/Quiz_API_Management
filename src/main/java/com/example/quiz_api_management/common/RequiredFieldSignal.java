package com.example.quiz_api_management.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/*
This class is design to return the signal of invalid RequestBody
 */
public class RequiredFieldSignal {
    private String field;
    private String cause;

    @Override
    public String toString() {
        return "RequiredFieldSignal{" +
                "field='" + field + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
