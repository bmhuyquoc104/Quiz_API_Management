package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.common.RequiredFieldSignal;
import com.example.quiz_api_management.common.ResponseReturn;
import com.example.quiz_api_management.exception.DuplicateException;
import com.example.quiz_api_management.exception.NotFoundException;
import com.example.quiz_api_management.question.Question;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController()
@RequestMapping(path = "/api/v1/")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }


    /*
    ok() method actually returns HTTP status OK.
    200 - OK returns when the request succeeded, and is allowed in GET, HEAD, PUT, POST & TRACE
     */

    @GetMapping("questions/{questionid}/answers")
    @ResponseBody
    public ResponseEntity<ResponseReturn> getAnswersByQuestion(@PathVariable("questionid") int questionId) {
        /*
            ofNullable() method in here is used to get an instance of this Optional class with Question Entity
            If the value is null, this method returns an empty instance.

            orElseThrow() in here is used to throw exceptions
            if an instance of Optional class with Question Entity is null

            => Check if question is not found. Otherwise, an exception is thrown.
        */
        Optional<Question> question = Optional.ofNullable(answerService.getQuestionById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        List<AnswerDTO> answersDTO = answerService.getAnswersByQuestion(question);
        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "List of answers is returned.",
                        HttpStatus.OK.value(),
                        true,
                        answersDTO), HttpStatus.OK);
    }


    @GetMapping("questions/{questionid}/answers/shuffle")
    public ResponseEntity<ResponseReturn> shuffleAnswers(@PathVariable("questionid") int questionId) {
        Optional<Question> question = Optional.ofNullable(answerService.getQuestionById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "Answers are shuffled.",
                        HttpStatus.OK.value(),
                        true,
                        answerService.shuffleAnswers(question)), HttpStatus.OK);
    }

    @GetMapping("questions/{questionid}/answers/{answerid}")
    public ResponseEntity<ResponseReturn> getAnswer(@PathVariable("questionid") int questionId,
                                                    @PathVariable("answerid") int answerId) {

        Optional<Question> question = Optional.ofNullable(answerService.getQuestionById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found")));

        // Like above, this statement is used to check if answer is not found.
        // If not found, an exception is thrown
        Optional<AnswerDTO> answerDTO = Optional.ofNullable(answerService.getAnswer(question, answerId)
                .orElseThrow(() -> new NotFoundException("Answer not found")));

        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                            "An answer is returned.",
                            HttpStatus.OK.value(),
                            true,
                            answerDTO), HttpStatus.OK);
    }

    /*
    201 - Created returns when the request succeeded, and a new resource was created as a result.
     */
    @PostMapping("questions/{questionid}/answers/add")
    public ResponseEntity<ResponseReturn> addAnswer(@PathVariable("questionid") int questionId,
                                                    @Valid @RequestBody AnswerDTO reqBody, BindingResult bindingResult) {
        Optional<Question> question = Optional.ofNullable(answerService.getQuestionById(questionId).orElseThrow(()
                -> new NotFoundException("Question not found")));

        /*
        bindingResult is used along with @Valid annotation.
        The purpose why it is used is that to catch if the fields in RequestBody are not valid.

        getField() to get the key of RequestBody, getDefaultMessage() to show why that field is not valid.

        => Check if RequestBody is valid.
         */
        if (bindingResult.hasErrors()) {
            List<RequiredFieldSignal> requiredFieldSignals = new ArrayList<>();
            String errorMessage = "";
            bindingResult.getFieldErrors().forEach(
                    fieldError -> {
                        requiredFieldSignals.add(
                                new RequiredFieldSignal(fieldError.getField(), fieldError.getDefaultMessage()));
                    }
            );
            for (RequiredFieldSignal requiredFieldSignal : requiredFieldSignals){
                errorMessage += "Field required: "+ requiredFieldSignal.getField() +
                        ", cause: "+ requiredFieldSignal.getCause() + " \n";
            }


            return new ResponseEntity<>(new ResponseReturn(LocalDateTime.now(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST.value(),
                    false,
                    null), HttpStatus.BAD_REQUEST);
        }

        // Check any answer's value is similar to the value of RequestBody
        if (answerService.notExistAnswer(question, reqBody).isPresent()) {
            throw new DuplicateException("Duplicate value found.");
        }

        AnswerDTO newAnswer = answerService.createAnswer(question, reqBody);
        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "New answer is added.",
                        HttpStatus.CREATED.value(),
                        true,
                        newAnswer), HttpStatus.CREATED);
    }

    /*
    400 - Bad Request status code indicates that the server cannot proceed.
     */
    @PutMapping("questions/{questionid}/answers/{answerid}/edit")
    public ResponseEntity<ResponseReturn> updateAnswer(@PathVariable("questionid") int questionId,
                                                       @PathVariable("answerid") int answerId,
                                                       @Valid @RequestBody AnswerDTO reqBody, BindingResult bindingResult) {

        // Check question is not found
        Optional<Question> question = Optional.ofNullable(answerService.getQuestionById(questionId).orElseThrow(()
                -> new NotFoundException("Question not found")));

        // Check answer is not found
        Optional.of(answerService.getAnswer(question, answerId).orElseThrow(()
                -> new NotFoundException("Answer not found")));

        // Check the RequestBody is not valid
        if (bindingResult.hasErrors()) {
            List<RequiredFieldSignal> requiredFieldSignals = new ArrayList<>();
            String errorMessage = "";
            bindingResult.getFieldErrors().forEach(
                    fieldError -> {
                        requiredFieldSignals.add(
                                new RequiredFieldSignal(fieldError.getField(), fieldError.getDefaultMessage()));
                    }
            );
            for (RequiredFieldSignal requiredFieldSignal : requiredFieldSignals){
                errorMessage += "Field required: "+ requiredFieldSignal.getField() +
                        ", cause: "+ requiredFieldSignal.getCause() + "\n";
            }

            return new ResponseEntity<>(new ResponseReturn(LocalDateTime.now(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST.value(),
                    false,
                    null), HttpStatus.BAD_REQUEST);
        }

        AnswerDTO returnAnswer = answerService.updateAnswer(answerId, reqBody);

        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "Successfully created.",
                        HttpStatus.OK.value(),
                        true,
                        returnAnswer), HttpStatus.OK);

    }

    /*
    HttpStatus - 204 - No Content means that there is no content to send in this request.
     */
    @DeleteMapping("questions/{questionid}/answers/{answerid}/delete")
    public ResponseEntity<ResponseReturn> deleteAnswer(@PathVariable("questionid") int questionId,
                                   @PathVariable("answerid") int answerId){

        Optional<Question> question = Optional.ofNullable(answerService.getQuestionById(questionId).orElseThrow(()
                -> new NotFoundException("Question not found")));

        Optional.of(answerService.getAnswer(question, answerId).orElseThrow(()
                -> new NotFoundException("Answer not found")));

        answerService.deleteAnswer(answerId);
        return new ResponseEntity<>(
                new ResponseReturn(LocalDateTime.now(),
                        "",
                        HttpStatus.NO_CONTENT.value(),
                        true,
                        null), HttpStatus.NO_CONTENT);
    }
}
