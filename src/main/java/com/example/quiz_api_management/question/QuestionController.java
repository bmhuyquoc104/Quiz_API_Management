package com.example.quiz_api_management.question;

import com.example.quiz_api_management.common.ResponseReturn;
import com.example.quiz_api_management.exception.DuplicateException;
import com.example.quiz_api_management.exception.NotFoundException;
import com.example.quiz_api_management.quiz.Quiz;

import com.example.quiz_api_management.util.RequestBodyError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/v1/")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(path="/questions")
    public ResponseEntity<ResponseReturn> paginateQuestions(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "filter", required = false) String filterType,
            @RequestParam(value = "order", required = false) String levelOrder){
        List<QuestionDTO> questions = questionService.paginateQuestions(page, filterType, levelOrder);
        if (questions == null){
            throw new NotFoundException("Pagination cannot be returned.");
        }
        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "Pagination of questions is returned.",
                        HttpStatus.OK.value(),
                        true,
                        questions), HttpStatus.OK);
    }

    @RequestMapping(path = "/quiz/{quizid}/questions")
    public ResponseEntity<ResponseReturn> getQuestionsByQuiz(@PathVariable("quizid") int quizId,
                                                             @RequestParam(value = "sort", required = false) String order,
                                                             @RequestParam(value = "filter", required = false) String filter) {

        Optional<Quiz> quiz = Optional.ofNullable(questionService.getQuizById(quizId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        List<QuestionDTO> questionsDTO = questionService.getQuestionsByQuiz(quiz);
        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                            "List of questions is returned.",
                            HttpStatus.OK.value(),
                            true,
                            questionsDTO), HttpStatus.OK);

    }


    @RequestMapping(path = "/questions/{questionid}")
    public ResponseEntity<ResponseReturn> getQuestion(@PathVariable("questionid") int questionId) {
        Optional<QuestionDTO> questionDTO = Optional.ofNullable(questionService.getQuestion(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "List of questions is returned.",
                        HttpStatus.OK.value(),
                        true,
                        questionDTO), HttpStatus.OK);
    }


    @RequestMapping(path = "/quiz/{quizid}/questions/add")
    public ResponseEntity<ResponseReturn> createQuestion(@PathVariable("quizid") int quizId,
                                                         @Valid @RequestBody QuestionDTO reqBody,
                                                         BindingResult bindingResult){
        Optional<Quiz> quiz = Optional.ofNullable(questionService.getQuizById(quizId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        if (questionService.notExistQuestion(quiz, reqBody).isPresent())
            throw new DuplicateException("Duplicate value found.");

        if (bindingResult.hasErrors()) {
            RequestBodyError error = new RequestBodyError();
            return error.returnRequiredFields(bindingResult);
        }

        QuestionDTO newQuestion = questionService.createQuestion(quiz, reqBody);
        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "New question is added",
                HttpStatus.CREATED.value(),
                true,
                newQuestion), HttpStatus.CREATED);
    }


    @RequestMapping(path = "/questions/{questionid}/edit")
    public ResponseEntity<ResponseReturn> updateQuestion(@PathVariable("questionid") int questionId,
                                                         @RequestBody QuestionDTO reqBody,
                                                         BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            RequestBodyError error = new RequestBodyError();
            return error.returnRequiredFields(bindingResult);
        }

        QuestionDTO newQuestion = questionService.updateQuestion(questionId, reqBody);
        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "New question is added",
                HttpStatus.CREATED.value(),
                true,
                newQuestion), HttpStatus.CREATED);
    }

    @RequestMapping(path="/questions/{questionid}/delete")
    public ResponseEntity<ResponseReturn> deleteQuestion(@PathVariable("questionid") int questionId){
        Optional.of(questionService.getQuestion(questionId).orElseThrow(()
                -> new NotFoundException("Question not found")));

        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "Question is deleted",
                HttpStatus.NO_CONTENT.value(),
                true,
                null), HttpStatus.NO_CONTENT);
    }
}
