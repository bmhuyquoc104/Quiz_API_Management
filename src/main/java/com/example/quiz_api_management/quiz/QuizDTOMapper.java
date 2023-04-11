package com.example.quiz_api_management.quiz;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class QuizDTOMapper implements Function<Quiz, QuizDTO> {
    @Override
    public QuizDTO apply(Quiz quiz){
        return new QuizDTO(
                quiz.getId(),
                quiz.getValue(),
                quiz.getType(),
                quiz.getQuestions().size(),
                quiz.getUpdatedAt(),
                quiz.getCreatedAt()
        );
    }
}
