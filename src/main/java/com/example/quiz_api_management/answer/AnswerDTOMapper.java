package com.example.quiz_api_management.answer;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/*
This Mapper exists to serve the function after streaming for DTO.
 */
@Service
public class AnswerDTOMapper implements Function<Answer, AnswerDTO> {
    @Override
    public AnswerDTO apply(Answer answer){
        return new AnswerDTO(
                answer.getId(),
                answer.getValue(),
                answer.isCorrect(),
                answer.isRemoved(),
                answer.getCreatedAt(),
                answer.getCreatedAt(),
                answer.getQuestion().getId());
    }
}
