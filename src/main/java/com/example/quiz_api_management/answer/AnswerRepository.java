package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.question.Question;
import org.springframework.data.repository.CrudRepository;


import java.util.List;
import java.util.Optional;

/*
    Notice that JpaRepository extends PagingAndSortingRepository which can also do methods such as pagination and sorting.
    While we just want to do CRUD operations for Answer model so that CrudRepository is simply fair.
 */
public interface AnswerRepository extends CrudRepository<Answer, Integer> {
    /*
    The idea of using Optional wrapper is to get rid of "null" value or object.
    In addition, this wrapper provides two methods which are isPresent() and get() for exception handling,
    While isPresent() return true if an object exists and get() will return the object otherwise an exception is thrown.
     */
    Optional<Answer> findById(int answerId);

    /*
    Simply, List in Java collection framework is provided the method shuffle().
    However, if the list of answers contain around 1 million answers, the method might not do sufficiently because it runs in linear time which is O(n)
    I just think that the shuffle method can be put in read operation in CRUD so CrudRepository is enough.
     */

    /*
    No need to use query, ORM can map relationship for this method.
     */
    List<Answer> findAnswerByQuestion(Optional<Question> question);
    Optional<Answer> findByName(String answerName);
}
