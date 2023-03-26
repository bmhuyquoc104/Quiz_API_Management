package com.example.quiz_api_management.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
DTO - Data Transfer Object used to serialize object
In this case, we just want only questionId not the whole of information of question which is mapped.
 */
@AllArgsConstructor
@Getter
@Setter
public class AnswerDTO {
    private int id;
    private String value;
    private boolean isCorrect;
    private boolean isRemoved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
