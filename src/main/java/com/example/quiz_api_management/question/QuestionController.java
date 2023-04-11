package com.example.quiz_api_management.question;

import com.example.quiz_api_management.common.PaginationReturn;
import com.example.quiz_api_management.common.ResponseReturn;
import com.example.quiz_api_management.exception.DuplicateException;
import com.example.quiz_api_management.exception.NotFoundException;
import com.example.quiz_api_management.exception.NotValidParams;
import com.example.quiz_api_management.quiz.Quiz;

import com.example.quiz_api_management.util.RequestBodyError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping(path="/questions")
    public ResponseEntity<PaginationReturn> paginateQuestions(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "filter", required = false) String filterQuery,
            @RequestParam(value = "sort", required = false) String sortQuery) {

        if (filterQuery != null && !questionService.checkValidTypeFilter(filterQuery))
            throw new NotValidParams("Check query for filtering.");

        if (sortQuery != null && !questionService.checkValidValueSort(sortQuery))
            throw new NotValidParams("Check query for sorting.");

        Page<QuestionDTO> paginationQuestion = questionService.paginateQuestions(page, filterQuery, sortQuery);
        int currentPage = questionService.getCurrentPage(paginationQuestion);
        return new ResponseEntity<>(
                new PaginationReturn(LocalDateTime.now(),
                        "Pagination of questions is returned.",
                        HttpStatus.OK.value(),
                        true,
                        paginationQuestion.getContent(),
                        currentPage,
                        paginationQuestion.getTotalPages(),
                        paginationQuestion.getNumberOfElements(),
                        paginationQuestion.getTotalElements(),
                        paginationQuestion.hasNext(),
                        paginationQuestion.hasPrevious()),
                        HttpStatus.OK);

    }

    @GetMapping(path = "/quiz/{quizid}/questions")
    public ResponseEntity<ResponseReturn> getQuestionsByQuiz(@PathVariable("quizid") int quizId) {

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


    @GetMapping(path = "/questions/{questionid}")
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


    @PostMapping(path = "/quiz/{quizid}/question")
    public ResponseEntity<ResponseReturn> createQuestion(@PathVariable("quizid") int quizId,
                                                         @Valid @RequestBody QuestionDTO reqBody,
                                                         BindingResult bindingResult){
        Optional<Quiz> quiz = Optional.ofNullable(questionService.getQuizById(quizId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        if (questionService.notExistQuestion(quiz, reqBody).isPresent())
            throw new DuplicateException("Duplicate value found.");

        if (bindingResult.hasErrors()) {
            return RequestBodyError.returnRequiredFields(bindingResult);
        }

        QuestionDTO newQuestion = questionService.createQuestion(quiz, reqBody);
        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "New question is added",
                HttpStatus.CREATED.value(),
                true,
                newQuestion), HttpStatus.CREATED);
    }


    @PutMapping(path = "/questions/{questionid}")
    public ResponseEntity<ResponseReturn> updateQuestion(@PathVariable("questionid") int questionId,
                                                         @RequestBody QuestionDTO reqBody,
                                                         BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return RequestBodyError.returnRequiredFields(bindingResult);
        }

        QuestionDTO newQuestion = questionService.updateQuestion(questionId, reqBody);
        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "New question is added",
                HttpStatus.CREATED.value(),
                true,
                newQuestion), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/questions/{questionid}")
    public ResponseEntity<ResponseReturn> deleteQuestion(@PathVariable("questionid") int questionId){
        questionService.getQuestion(questionId).orElseThrow(()
                -> new NotFoundException("Question not found"));

        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "Question is deleted",
                HttpStatus.NO_CONTENT.value(),
                true,
                null), HttpStatus.NO_CONTENT);
    }


}
