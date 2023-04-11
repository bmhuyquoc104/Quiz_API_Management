package com.example.quiz_api_management.question;

import com.example.quiz_api_management.common.OrderSort;
import com.example.quiz_api_management.quiz.Quiz;
import com.example.quiz_api_management.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<QuestionDTO> paginateQuestions(int page, String filterQuery, String sortQuery){
        Pageable pageable;
        // Due to first page always be 0 (Page class)
        page--;
        if (sortQuery != null)
            pageable = sortedPageable(page , sortQuery);
        else {
            int defaultSize = 10;
            pageable = PageRequest.of(page , defaultSize);
        }
        return filterPageRequest(pageable, filterQuery);
    }

    public Page<QuestionDTO> filterPageRequest(Pageable pageable, String filterType){
        if (filterType != null)
            return questionRepository.findQuestionByType(filterType.toLowerCase(), pageable).map(questionDTOMapper);
        else
            return questionRepository.findAll(pageable).map(questionDTOMapper);
    }

    public PageRequest sortedPageable(int page, String propertySort){
        int defaultSize = 10;
        propertySort = propertySort.toLowerCase();
        String valueType = propertySort.split(",")[0];
        String levelOrder = propertySort.split(",")[1];
        if (Objects.equals(levelOrder, OrderSort.ASC.levelOrder)) {
           return PageRequest.of(page, defaultSize,
                    Sort.by(valueType).ascending()); // Option for sorting by value with descending order
        }
        else  {
           return PageRequest.of(page, defaultSize,
                    Sort.by(valueType).descending()); // Option for sorting by value with descending order
        }
    }

    public boolean checkValidTypeFilter(String filterType){
        // Check if filterType belongs to {"multiple", "long", "short"}
        return QuestionFilter.contains(filterType);
    }


    /* Check if propertySort is a valid string, propertySort should be "value,desc" or "value,asc"
    Take substring of propertySort before "," to get "value"
    Take substring after "," to get "asc" or "desc", valueSort should be "desc" or "asc" */
    public boolean checkValidValueSort(String propertySort){
        propertySort = propertySort.toLowerCase();
        String valueSort = propertySort.split(",")[0];
        String levelSort = propertySort.split(",")[1];
        return OrderSort.contains(levelSort) && Objects.equals(valueSort, "value");
    }

    // Should be incremented again due to first page always be 0 (Page class)
    public int getCurrentPage(Page<QuestionDTO> paginationQuestion){
        return paginationQuestion.getNumber() + 1;
    }


}


