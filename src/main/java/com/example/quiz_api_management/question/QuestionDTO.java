package com.example.quiz_api_management.question;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class QuestionDTO {
    private int id;

    @NotNull
    @Size(min = 3, max = 50)
    private String value;

    @NotNull
    @Size(min = 3, max = 10)
    private String type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private int quizId;

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", quizId=" + quizId +
                '}';
    }
}
