package com.example.quiz_api_management.question;

import com.example.quiz_api_management.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findById(int questionId);

    List<Question> findQuestionsByQuiz(Optional<Quiz> quiz);


}
