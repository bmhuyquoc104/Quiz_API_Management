package com.example.quiz_api_management.question;

import com.example.quiz_api_management.common.OrderSort;
import com.example.quiz_api_management.quiz.Quiz;
import com.example.quiz_api_management.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final QuestionDTOMapper questionDTOMapper;
    @Autowired
    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository, QuestionDTOMapper questionDTOMapper){
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.questionDTOMapper = questionDTOMapper;
    }

    public Optional<Quiz> getQuizById(int quizId){
        return quizRepository.findById(quizId);
    }

    public List<QuestionDTO> getQuestionsByQuiz(Optional<Quiz> quiz){
        return questionRepository.findQuestionsByQuiz(quiz)
                .stream()
                .map(questionDTOMapper)
                .toList();
    }

    public Optional<QuestionDTO> getQuestion(int questionId) {
        return questionRepository.findById(questionId).map(questionDTOMapper);
    }

    public Optional<Question> notExistQuestion(Optional<Quiz> paramQuiz, QuestionDTO reqBody){
        List<Question> questions = questionRepository.findQuestionsByQuiz(paramQuiz);
        return questions
                .stream()
                .filter(question -> (Objects.equals(reqBody.getValue(), question.getValue())))
                .findAny();
    }

    public QuestionDTO createQuestion(Optional<Quiz> paramQuiz, QuestionDTO reqBody){
        Quiz quiz = paramQuiz.get();
        Question addedQuestion = new Question(reqBody.getValue(), reqBody.getType(), quiz);
        questionRepository.save(addedQuestion);
        List<Question> questions = questionRepository.findQuestionsByQuiz(Optional.of(quiz)).stream().toList();
        return questions.stream()
                .filter(question -> (Objects.equals(question.getValue(), reqBody.getValue())))
                .findAny()
                .map(questionDTOMapper).get();
    }


    public QuestionDTO updateQuestion(int questionId, QuestionDTO reqBody){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.get();
        question.setValue(reqBody.getValue());
        question.setType(reqBody.getType());
        question.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question);
        Optional<Question> newQuestion = questionRepository.findById(questionId);
        return newQuestion.isPresent() ? newQuestion.map(questionDTOMapper).get() : null;
    }


    public void deleteQuestion(int questionId){
        questionRepository.deleteById(questionId);
    }


    //Pagination + Filtering + Sorting by 'value' with ASC or DESC order
    public List<QuestionDTO> paginateQuestions(int page, String filterType, String levelSort){
        int defaultSize = 10;
        Pageable pageable;
        // Check if levelSort is a valid string and levelSort belongs to {"asc", "desc"}
        if (levelSort != null && OrderSort.contains(levelSort))
            pageable = sortedPageable(page, levelSort);
        // Check if levelSort is not valid for ordering, does not belong to {"asc", "desc"}
        else if (levelSort != null)
            return null;
        else
            pageable = PageRequest.of(page, defaultSize);
        return filterPageRequest(pageable, filterType);
    }

    public List<QuestionDTO> filterPageRequest(Pageable pageable, String filterType){
        // Check if filterType belongs to {"multiple", "long", "short"}
        if (filterType != null && QuestionFilter.contains(filterType))
            return questionRepository
                    .findQuestionByType(filterType, pageable)
                    .stream()
                    .map(questionDTOMapper)
                    .toList();
        // Check if filterType is not valid for question types, does not belong to {"multiple", "long", "short"}
        else if (filterType != null)
            return null;
        else
            return questionRepository
                    .findAll(pageable)
                    .stream()
                    .map(questionDTOMapper)
                    .toList();
    }

    public PageRequest sortedPageable(int page, String orderLevel){
        if (Objects.equals(orderLevel, OrderSort.ASC.levelOrder)) {
           return PageRequest.of(page, 10,
                    Sort.by("value").descending()); // Option for sorting by value with descending order
        }
        else  {
           return PageRequest.of(page, 10,
                    Sort.by("value").ascending()); // Option for sorting by value with descending order

        }
    }
}
