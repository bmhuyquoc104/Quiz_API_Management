package com.example.quiz_api_management.answer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/*
DTO - Data Transfer Object used to serialize object
In this case, we just want only questionId not the whole of information of question which is mapped.
 */
@AllArgsConstructor
@Getter
@Setter
public class AnswerDTO {
    private int id;
    /*
        These annotations are used for validation by Spring Boot, no need to manually validate.
        @NotNull - Field 'value' should not be null
        @Size - Limits the length of string field 'value'
     */
    @NotNull
    @Size(min = 1, max = 20)
    private String value;
    @NotNull
    private boolean isCorrect;
    private boolean isRemoved;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private int questionId;

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", isCorrect=" + isCorrect +
                ", isRemoved=" + isRemoved +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", questionId=" + questionId +
                '}';
    }
}
