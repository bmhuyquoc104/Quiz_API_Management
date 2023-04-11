package com.example.quiz_api_management.quiz;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Data
public class QuizDTO {
    private int id;
    @NotNull
    @Size(min = 3, max= 50)
    private String value;

    @NotNull
    @Size(min = 3, max= 50)
    private String type;

    private int questionCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public QuizDTO(String value, String type){
        this.value = value;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.questionCount = 0;
    }
    public QuizDTO() {
    }
}
