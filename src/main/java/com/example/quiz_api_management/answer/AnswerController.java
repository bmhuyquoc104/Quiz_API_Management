package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.common.ResponseReturn;
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
    public AnswerController(AnswerService answerService){
        this.answerService = answerService;
    }

    /*
    ok() method actually returns HTTP status OK.
    200 - OK returns when the request succeeded, and is allowed in GET, HEAD, PUT, POST & TRACE
     */
    @GetMapping("questions/{questionid}/answers")
    @ResponseBody
    public ResponseEntity<ResponseReturn> getAnswersByQuestion(@PathVariable("questionid") int questionId){
        List<AnswerDTO> answersDTO = answerService.getAnswersByQuestion(questionId);
        return new ResponseEntity<>(
                new ResponseReturn("List of answer is returned.",
                                200,
                                true,
                                answersDTO), HttpStatus.OK);
    }


    @GetMapping("questions/{questionid}/answers/shuffle")
    public ResponseEntity<ResponseReturn> shuffleAnswers(@PathVariable("questionid") int questionId) {
        return new ResponseEntity<>(
                new ResponseReturn("Answers are shuffled.",
                        200,
                        true,
                        answerService.shuffleAnswers(questionId)), HttpStatus.OK);
    }

    @GetMapping("questions/{questionid}/answers/{answerid}")
    public ResponseEntity<ResponseReturn> getAnswer(@PathVariable("questionid") int questionId,
                               @PathVariable("answerid") int answerid) {
        return new ResponseEntity<>(
                new ResponseReturn("An answer is returned.",
                        200,
                        true,
                        answerService.getAnswer(questionId, answerid)), HttpStatus.OK);
    }

    /*
    201 - Created returns when the request succeeded, and a new resource was created as a result.
     */
    @PostMapping("questions/{questionid}/answers/add")
    public ResponseEntity<ResponseReturn> addAnswer(@PathVariable("questionid") int questionId,
                                         @RequestBody AnswerDTO reqBody){
        AnswerDTO newAnswer = answerService.addAnswer(questionId, reqBody);
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
    public  ResponseEntity<ResponseReturn> updateAnswer(@PathVariable("questionid") int questionId,
                             @PathVariable("answerid") int answerId,
                             @RequestBody AnswerDTO reqBody){
        if (reqBody == null){
            return new ResponseEntity<>(
                    new ResponseReturn("Missing required fields.",
                            400,
                            false,
                            null), HttpStatus.NOT_ACCEPTABLE);
        }
        AnswerDTO updatedAnswer = answerService.updateAnswer(questionId, answerId, reqBody);
        return new ResponseEntity<>(
                new ResponseReturn("Answer is updated.",
                201,
                true,
                updatedAnswer), HttpStatus.OK);
    }


    /*
    HttpStatus - 204 - No Content means that there is no content to send in this request.
     */
    @DeleteMapping("questions/{questionid}/answers/{answerid}/delete")
    public ResponseEntity<ResponseReturn> deleteAnswer(@PathVariable("questionid") int questionId,
                                   @PathVariable("answerid") int answerId){
        answerService.deleteAnswer(questionId, answerId);
        return new ResponseEntity<>(
                new ResponseReturn("Deleted an answer with answerId: " + answerId,
                        204,
                        true,
                        null), HttpStatus.NO_CONTENT);
    }
}
