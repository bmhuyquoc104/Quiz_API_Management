package com.example.quiz_api_management.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Optional<Quiz> findById(int quiz_id);

    Optional<Quiz> findByValue(String value);

    /*
    Customize query for getting list of quizzes by sorting number of questions
    This serves for pagination.
     */
    @Query(value = "SELECT quiz FROM Quiz quiz ORDER BY (SELECT COUNT(question) FROM quiz.questions question) DESC")
    Page<Quiz> sortQuestionByCountDesc(String sortByCount, Pageable pageable);

    @Query(value = "SELECT quiz FROM Quiz quiz ORDER BY (SELECT COUNT(question) FROM quiz.questions question) ASC")
    Page<Quiz> sortQuestionByCountAsc(String sortByCount, Pageable pageable);
}
