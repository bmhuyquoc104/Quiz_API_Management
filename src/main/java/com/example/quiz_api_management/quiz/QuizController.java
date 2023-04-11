package com.example.quiz_api_management.quiz;

import com.example.quiz_api_management.common.PaginationReturn;
import com.example.quiz_api_management.common.ResponseReturn;
import com.example.quiz_api_management.exception.DuplicateException;
import com.example.quiz_api_management.exception.NotFoundException;
import com.example.quiz_api_management.exception.NotValidParams;
import com.example.quiz_api_management.util.RequestBodyError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService){
        this.quizService = quizService;
    }

    @GetMapping(path = "/quizzes")
    public ResponseEntity<PaginationReturn> paginateQuizzes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sort", required = false) String sortQuery) {
        if (sortQuery != null && !quizService.checkValidSortQuery(sortQuery))
            throw new NotValidParams("Check query for sorting.");

        Page<QuizDTO> paginationQuiz = quizService.paginateQuizzes(page, sortQuery);
        int currentPage = quizService.getCurrentPage(paginationQuiz);
        return new ResponseEntity<>(
                new PaginationReturn(LocalDateTime.now(),
                        "Pagination of quizzes is returned.",
                        HttpStatus.OK.value(),
                        true,
                        paginationQuiz.getContent(),
                        currentPage,
                        paginationQuiz.getTotalPages(),
                        paginationQuiz.getNumberOfElements(),
                        paginationQuiz.getTotalElements(),
                        paginationQuiz.hasNext(),
                        paginationQuiz.hasPrevious()), HttpStatus.OK);
    }

    @GetMapping(path = "/quizzes/{quizid}")
    public ResponseEntity<ResponseReturn> getQuiz(@RequestParam("quizid") int quizId){
        Optional<QuizDTO> quizDTO = Optional.ofNullable(quizService.getQuiz(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found")));

        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "A quiz is returned.",
                HttpStatus.OK.value(),
                true,
                quizDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/quizzes")
    public ResponseEntity<ResponseReturn> createQuiz(@Valid @RequestBody QuizDTO reqBody,
                                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return RequestBodyError.returnRequiredFields(bindingResult);
        }

        if (quizService.notExistQuiz(reqBody).isPresent())
            throw new DuplicateException("Duplicate value found.");

        QuizDTO newQuiz = quizService.createQuiz(reqBody);

        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "New quiz is created.",
                HttpStatus.CREATED.value(),
                true,
                newQuiz), HttpStatus.CREATED);
    }

    @PutMapping(path = "/quizzes/{quizid}")
    public ResponseEntity<ResponseReturn> updateQuiz(@RequestParam("quizid") int quizId,
                                                     @Valid @RequestBody QuizDTO reqBody,
                                                     BindingResult bindingResult){
        quizService.getQuiz(quizId).orElseThrow(()
                -> new NotFoundException("Quiz not found"));

        if (bindingResult.hasErrors()) {
            return RequestBodyError.returnRequiredFields(bindingResult);
        }

        if (quizService.notExistQuiz(reqBody).isPresent())
            throw new DuplicateException("Duplicate value found.");

        QuizDTO updatedQuiz = quizService.updateQuiz(quizId, reqBody);

        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "A quiz is updated.",
                HttpStatus.ACCEPTED.value(),
                true,
                updatedQuiz), HttpStatus.ACCEPTED);

    }

    @DeleteMapping(path = "/quizzes/{quizid}")
    public ResponseEntity<ResponseReturn> deleteQuiz(@RequestParam("quizid") int quizId){
        quizService.getQuiz(quizId).orElseThrow(()
                -> new NotFoundException("Quiz not found"));

        quizService.deleteQuiz(quizId);

        return new ResponseEntity<>(new ResponseReturn(
                LocalDateTime.now(),
                "A quiz is deleted.",
                HttpStatus.NO_CONTENT.value(),
                true,
                null), HttpStatus.NO_CONTENT);
    }
}
