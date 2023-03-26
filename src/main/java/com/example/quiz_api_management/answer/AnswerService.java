package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.question.Question;
import com.example.quiz_api_management.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final AnswerDTOMapper answerDTOMapper;
    @Autowired
    public AnswerService(AnswerRepository answerRepository, AnswerDTOMapper answerDTOMapper, QuestionService questionService){
        this.answerRepository = answerRepository;
        this.answerDTOMapper = answerDTOMapper;
        this.questionService = questionService;
    }

    /*
    Though IllegalStateException return a signal if function invokes at an illegal or inappropriate time, it is not a good case for this scenario
    Using EntityNotFoundException will be appropriate in this scenario, due to the meaning of NotFound.
     */

    public Optional<Question> getQuestionById(int questionId){
        return questionService.getQuestion(questionId);
    }

    public List<AnswerDTO> getAnswersByQuestion(Optional<Question> question) {
        return answerRepository.findAnswerByQuestion(question)
                .stream()
                .map(answerDTOMapper)
                .toList();
    }

    /*
    We should shuffle list of model Answer instead of list of answerDTO, due to answerDTO will also have questionId,
    and it will be immutable (cause is maybe having 2 integers to order). It does not shuffle if the collection is immutable
     */
    public List<AnswerDTO> shuffleAnswers(Optional<Question> question) {
        List<Answer> answers = answerRepository.findAnswerByQuestion(question);
        Collections.shuffle(answers);
        return answers.stream().map(answerDTOMapper).toList();
    }

    /*
    public AnswerDTO getAnswer(int answerId){
        Optional <Answer> answer = answerRepository.findById(answerId);
        return answer.isPresent() ? answer.map(answerDTOMapper).get() : null;
    }

     */

    public AnswerDTO getAnswer(Optional<Question> question, int answerId){
        Optional<Answer> optionalAnswer = answerRepository.findAnswerByQuestion(question)
                .stream()
                .filter(answer -> (answerId == answer.getId())) // Find if that answer.Id == params.answerId
                .findAny();
        return optionalAnswer.isPresent() ? optionalAnswer.map(answerDTOMapper).get() : null;
    }

    public AnswerDTO addAnswer(Optional<Question> paramQuestion, AnswerDTO reqBody) {
        Question question = paramQuestion.get();
        Answer addedAnswer = new Answer(reqBody.getValue(), reqBody.isCorrect(), question);
        answerRepository.save(addedAnswer);
        Optional<Answer> newAnswer = answerRepository.findByValue(addedAnswer.getValue());
        return newAnswer.isPresent() ? newAnswer.map(answerDTOMapper).get() : null;
    }


    /*
    Annotation @Transactional provokes the rollback if an exception occurs.
     */
    @Transactional
    public AnswerInputValidation updateAndValidate(int answerId, AnswerDTO reqBody) {
        AnswerInputValidation answerInputValidation = new AnswerInputValidation(reqBody);
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (answerInputValidation.isAccepted()) {
            updateAnswer(optionalAnswer, reqBody);
            AnswerDTO responseAnswer = optionalAnswer.map(answerDTOMapper).get();
            answerInputValidation.setAnswer(responseAnswer);
        }
        return answerInputValidation;
    }

    public void updateAnswer(Optional<Answer> optionalAnswer, AnswerDTO reqBody){
        Answer answer = optionalAnswer.get();
        answer.setValue(reqBody.getValue());
        answer.setCorrect(reqBody.isCorrect());
        answerRepository.save(answer);
    }

    public void deleteAnswer(int answerId) {
        answerRepository.deleteById(answerId);
    }
}
