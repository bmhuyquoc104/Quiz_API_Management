package com.example.quiz_api_management.quiz;

import com.example.quiz_api_management.common.OrderSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizDTOMapper quizDTOMapper;
    @Autowired
    public QuizService(QuizRepository quizRepository, QuizDTOMapper quizDTOMapper){
        this.quizRepository = quizRepository;
        this.quizDTOMapper = quizDTOMapper;
    }

    public Optional<QuizDTO> getQuiz(int quizId) {
        return quizRepository.findById(quizId).map(quizDTOMapper);
    }

    public Optional<Quiz> notExistQuiz(QuizDTO reqBody){
        return quizRepository.findByValue(reqBody.getValue());
    }


    public QuizDTO createQuiz(QuizDTO requestBody){
        Quiz newQuiz = new Quiz(requestBody.getValue(), requestBody.getType());
        quizRepository.save(newQuiz);
        return quizRepository.findByValue(requestBody.getValue())
                .stream()
                .findAny()
                .map(quizDTOMapper)
                .get();
    }

    public QuizDTO updateQuiz(int quizId, QuizDTO requestBody) {
        Quiz quiz = quizRepository.findById(quizId).get();
        quiz.setValue(requestBody.getValue());
        quiz.setUpdatedAt(LocalDateTime.now());
        quizRepository.save(quiz);
        Optional<Quiz> newQuiz = quizRepository.findById(quizId);
        return newQuiz.isPresent() ? newQuiz.map(quizDTOMapper).get() : null;
    }

    public void deleteQuiz(int quizId) {
        quizRepository.deleteById(quizId);
    }

    public boolean checkValidSortQuery(String sortQuery){
        sortQuery = sortQuery.toLowerCase();
        String optionSort = sortQuery.split(",")[0];
        String levelSort = sortQuery.split(",")[1];
        return OrderSort.contains(levelSort) && QuizQuery.contains(optionSort);
    }

    public Page<QuizDTO> paginateQuizzes(int page, String sortQuery){
        Pageable pageable;
        // Due to first page always be 0 (Page class)
        page--;
        String optionSort = sortQuery.split(",")[0].toLowerCase();
        if (Objects.equals(optionSort, "type"))
            pageable = sortedPageable(page, sortQuery);
        else if (Objects.equals(optionSort, "count")) {
            return sortByCountQuestionQuizzes(page, sortQuery);
        }
        else {
            int defaultSize = 10;
            pageable = PageRequest.of(page, defaultSize);
        }
        return quizRepository
                .findAll(pageable)
                .map(quizDTOMapper);
    }


    public Page<QuizDTO> sortByCountQuestionQuizzes(int page, String sortQuery){
        int defaultSize = 10;
        Pageable pageable = PageRequest.of(page, defaultSize);
        String levelOrder = sortQuery.split(",")[1].toLowerCase();
        if (levelOrder.equals("asc"))
            return quizRepository
                    .sortQuestionByCountAsc(sortQuery, pageable)
                    .map(quizDTOMapper);
        else
            return quizRepository
                    .sortQuestionByCountDesc(sortQuery, pageable)
                    .map(quizDTOMapper);

    }

    public PageRequest sortedPageable(int page, String propertySort){
        int defaultSize = 10;
        propertySort = propertySort.toLowerCase();
        String valueType = propertySort.split(",")[0];
        String levelOrder = propertySort.split(",")[1];
        if (Objects.equals(levelOrder, OrderSort.ASC.levelOrder)) {
            return PageRequest.of(page, defaultSize,
                    Sort.by(valueType).ascending());
        }
        else  {
            return PageRequest.of(page, defaultSize,
                    Sort.by(valueType).descending());
        }
    }

    public int getCurrentPage(Page<QuizDTO> paginationQuiz){
        return paginationQuiz.getNumber() + 1;
    }
}
