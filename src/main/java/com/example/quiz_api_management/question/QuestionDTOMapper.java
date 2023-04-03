package com.example.quiz_api_management.question;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class QuestionDTOMapper implements Function<Question, QuestionDTO> {
    @Override
    public QuestionDTO apply(Question question){
        return new QuestionDTO(
                question.getId(),
                question.getValue(),
                question.getType(),
                question.getCreatedAt(),
                question.getUpdatedAt(),
                question.getQuiz().getId());
    }
}
