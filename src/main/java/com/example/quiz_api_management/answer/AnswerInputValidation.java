package com.example.quiz_api_management.answer;

import lombok.Getter;

@Getter
public class AnswerInputValidation {
    private final int MAX_LENGTH_VALUE = 60;
    private final int ID_EMPTY = 0;
    private boolean accepted = true;
    private String message = "Successfully Updated.";
    private Object answer = null;
    private boolean hasSpecialCharacter(String value){
        return value.contains("!") || value.contains("@") ||
                value.contains("?") || value.contains("<") ||
                value.contains(">");
    }

    private boolean hasProperLength(String value){
        return value.length() <= MAX_LENGTH_VALUE;
    }

    private boolean checkEmptyBody(AnswerDTO reqBody){
        /* Really tricky, because reqBody is a class,
        if a class is deserialized from json and is initially empty, it will create default values
         */

        /*
        In this case, we will check if the request body is empty following with default values.
        The request body is empty if it satisfies: ID = 0 && value = null && isCorrect = false.
         */
        return reqBody.getId() == ID_EMPTY && reqBody.getValue() == null && !reqBody.isCorrect();
    }

    private boolean isNull(String value){
        return value.isBlank();
    }

    private void checkValue(String value) {
        if (isNull(value)) {
            this.accepted = false;
            this.message = "Value is null.";
        }
        if (!hasProperLength(value)) {
            this.accepted = false;
            this.message = "Value does not have appropriate length.";
        }

        if (hasSpecialCharacter(value)){
            this.accepted = false;
            this.message = "Value has special character(s)";
        }

    }
    public AnswerInputValidation(AnswerDTO reqBody) {
        if (checkEmptyBody(reqBody)) {
            this.accepted = false;
            this.message = "Field(s) should be updated.";
        }
        else {
            checkValue(reqBody.getValue());
        }
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }
}
