package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.question.Question;
import com.example.quiz_api_management.question.QuestionService;
import jakarta.persistence.EntityNotFoundException;
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
        Optional<Question> question = questionService.getQuestion(questionId);
        if (question.isEmpty()) {
            throw new IllegalStateException("Question not found.");
        }
        return question;
    }

    public List<AnswerDTO> getAnswersByQuestion(int questionId) {
        Optional<Question> question = getQuestionById(questionId);
        return answerRepository.findAnswerByQuestion(question)
                .stream()
                .map(answerDTOMapper)
                .toList();
    }

    /*
    We should shuffle list of model Answer instead of list of answerDTO, due to answerDTO will also have questionId,
    and it will be immutable (cause is maybe having 2 integers to order). It does not shuffle if the collection is immutable
     */
    public List<AnswerDTO> shuffleAnswers(int questionId) {
        Optional<Question> question = getQuestionById(questionId);
        List<Answer> answers = answerRepository.findAnswerByQuestion(question);
        Collections.shuffle(answers);
        return answers.stream().map(answerDTOMapper).toList();
    }

    public AnswerDTO getAnswer(int questionId, int answerId){
        if (getQuestionById(questionId).isEmpty()){
            throw new EntityNotFoundException("Cannot find answer due to question is not found.");
        }
        Optional <Answer> answer = answerRepository.findById(answerId);
        return answer.isPresent() ? answer.map(answerDTOMapper).get() : null;
    }

    public AnswerDTO addAnswer(int questionId, AnswerDTO reqBody) {
        Optional<Question> paramQuestion = getQuestionById(questionId);
        if (paramQuestion.isEmpty()) {
            throw new EntityNotFoundException("Question not found.");
        }
        answerRepository.save(new Answer(reqBody.getName(), reqBody.isCorrect(), paramQuestion.get()));
        Optional<Answer> newAnswer = answerRepository.findByName(reqBody.getName());
        return newAnswer.isPresent() ? newAnswer.map(answerDTOMapper).get() : null;
    }


    /*
    Annotation @Transactional provokes the rollback if an exception occurs.
     */
    @Transactional
    public AnswerDTO updateAnswer(int questionId, int answerId, AnswerDTO reqBody) {
        if (getAnswersByQuestion(questionId) == null){
            throw new EntityNotFoundException("Cannot find answer due to question is not found.");
        }

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if(optionalAnswer.isEmpty()){
            throw new EntityNotFoundException("Cannot find answer due to question is not found.");
        }

        Answer answer = optionalAnswer.get();
        /* I think validation for Answer is enough in this part. */
        if(reqBody.getName() != null) {
            answer.setName(reqBody.getName());
            answer.setCorrect(reqBody.isCorrect());
            answerRepository.save(answer);
        }
        return optionalAnswer.map(answerDTOMapper).get();
    }

    public void deleteAnswer(int questionId, int answerId) {
        if (getAnswersByQuestion(questionId) == null){
            throw new EntityNotFoundException("Cannot find answers due to question is not found.");
        }
        boolean exists = answerRepository.existsById(answerId);
        if (!exists) {
            throw new EntityNotFoundException("Answer with id" + answerId + "is not valid.");
        }
        answerRepository.deleteById(answerId);
    }
}
