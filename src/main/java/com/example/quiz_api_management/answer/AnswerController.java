package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.common.ResponseReturn;
import com.example.quiz_api_management.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Optional<Question> question = answerService.getQuestionById(questionId);
        if (question.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot return question, check the parameter.",
                            404,
                            false,
                            null), HttpStatus.BAD_REQUEST);

        }
        List<AnswerDTO> answersDTO = answerService.getAnswersByQuestion(question);
        return new ResponseEntity<>(
                new ResponseReturn("List of answers is returned.",
                        200,
                        true,
                        answersDTO), HttpStatus.OK);
    }


    @GetMapping("questions/{questionid}/answers/shuffle")
    public ResponseEntity<ResponseReturn> shuffleAnswers(@PathVariable("questionid") int questionId) {
        Optional<Question> question = answerService.getQuestionById(questionId);
        if (question.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot return question, check the parameter.",
                            404,
                            false,
                            null), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                new ResponseReturn("Answers are shuffled.",
                        200,
                        true,
                        answerService.shuffleAnswers(question)), HttpStatus.OK);
    }

    @GetMapping("questions/{questionid}/answers/{answerid}")
    public ResponseEntity<ResponseReturn> getAnswer(@PathVariable("questionid") int questionId,
                                                    @PathVariable("answerid") int answerId) {
        Optional<Question> question = answerService.getQuestionById(questionId);
        if (question.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot return question, check the parameter.",
                            404,
                            false,
                            null), HttpStatus.BAD_REQUEST);

        }

        AnswerDTO answerDTO = answerService.getAnswer(question, answerId);
        if (answerDTO == null) {
            return new ResponseEntity<>(
                    new ResponseReturn("Answer not found, check the parameter again.",
                            404,
                            false,
                            null), HttpStatus.NOT_FOUND);

        }
        else {
            return new ResponseEntity<>(
                    new ResponseReturn("An answer is returned.",
                            200,
                            true,
                            answerDTO), HttpStatus.OK);
        }
    }

    /*
    201 - Created returns when the request succeeded, and a new resource was created as a result.
     */
    @PostMapping("questions/{questionid}/answers/add")
    public ResponseEntity<ResponseReturn> addAnswer(@PathVariable("questionid") int questionId,
                                                    @RequestBody AnswerDTO reqBody) {
        Optional<Question> question = answerService.getQuestionById(questionId);
        if (question.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot return question, check the parameter.",
                            404,
                            false,
                            null), HttpStatus.BAD_REQUEST);

        }

        AnswerDTO newAnswer = answerService.addAnswer(question, reqBody);
        return new ResponseEntity<>(
                new ResponseReturn("New answer is added.",
                        201,
                        true,
                        newAnswer), HttpStatus.CREATED);
    }

    /*
    400 - Bad Request status code indicates that the server cannot proceed.
    I think validation for Answer is enough in this part.
     */
    @PutMapping("questions/{questionid}/answers/{answerid}/edit")
    public ResponseEntity<ResponseReturn> updateAnswer(@PathVariable("questionid") int questionId,
                                                       @PathVariable("answerid") int answerId,
                                                       @RequestBody AnswerDTO reqBody) {
        Optional<Question> question = answerService.getQuestionById(questionId);
        if (question.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot return question, check the parameter.",
                            404,
                            false,
                            null), HttpStatus.NOT_FOUND);

        }

        AnswerDTO answer = answerService.getAnswer(question, answerId);
        if (answer == null) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot update answer because answer is not found.",
                            404,
                            false,
                            null), HttpStatus.NOT_FOUND);

        }
        AnswerInputValidation answerInputValidation = answerService.updateAndValidate(answerId, reqBody);
        if (answerInputValidation.isAccepted()) {
            return new ResponseEntity<>(
                    new ResponseReturn(answerInputValidation.getMessage(),
                            201,
                            true,
                            answerInputValidation.getAnswer()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    new ResponseReturn(answerInputValidation.getMessage(),
                            400,
                            false,
                            answerInputValidation.getAnswer()), HttpStatus.BAD_REQUEST);
        }
    }




    /*
    HttpStatus - 204 - No Content means that there is no content to send in this request.
     */
    @DeleteMapping("questions/{questionid}/answers/{answerid}/delete")
    public ResponseEntity<ResponseReturn> deleteAnswer(@PathVariable("questionid") int questionId,
                                   @PathVariable("answerid") int answerId){
        Optional<Question> question = answerService.getQuestionById(questionId);
        if (question.isEmpty()){
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot return question, check the parameter.",
                            404,
                            false,
                            null), HttpStatus.BAD_REQUEST);

        }

        AnswerDTO answer = answerService.getAnswer(question, answerId);
        if (answer == null) {
            return new ResponseEntity<>(
                    new ResponseReturn("Cannot delete answer because answer is not found.",
                            404,
                            false,
                            null), HttpStatus.NOT_FOUND);

        }

        answerService.deleteAnswer(answerId);
        return new ResponseEntity<>(
                new ResponseReturn("Deleted an answer with answerId: " + answerId,
                        204,
                        true,
                        null), HttpStatus.NO_CONTENT);
    }
}
