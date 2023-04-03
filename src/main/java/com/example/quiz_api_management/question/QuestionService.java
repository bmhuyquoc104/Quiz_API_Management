package com.example.quiz_api_management.question;

import com.example.quiz_api_management.quiz.Quiz;
import com.example.quiz_api_management.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuestionService {
    private final String ASCENDING_ORDER = "ASC";
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

    public List<QuestionDTO> sortQuestions(Optional<Quiz> quiz, String order){
        if(order == ASCENDING_ORDER) return questionRepository.findQuestionsByQuiz(quiz)
                .stream()
                .sorted(Comparator.comparing(Question::getValue))
                .map(questionDTOMapper)
                .toList();
        else
             return questionRepository.findQuestionsByQuiz(quiz)
                .stream()
                .sorted(Comparator.comparing(Question::getValue).reversed())
                .map(questionDTOMapper)
                .toList();
    }


    public List<QuestionDTO> filterQuestionsByType(Optional<Quiz> quiz, String type){
        return questionRepository.findQuestionsByQuiz(quiz)
                .stream()
                .filter(question -> (Objects.equals(question.getType(), type)))
                .map(questionDTOMapper)
                .toList();
    }

    public List<QuestionDTO> filterAndSortQuestions(Optional<Quiz> quiz, String type, String order){
        if (order == ASCENDING_ORDER)
            return questionRepository.findQuestionsByQuiz(quiz)
                            .stream()
                            .filter(question -> (Objects.equals(question.getType(), type)))
                            .sorted(Comparator.comparing(Question::getValue))
                            .map(questionDTOMapper)
                            .toList();

        else
            return questionRepository.findQuestionsByQuiz(quiz)
                    .stream()
                    .filter(question -> (Objects.equals(question.getType(), type)))
                    .sorted(Comparator.comparing(Question::getValue).reversed())
                    .map(questionDTOMapper)
                    .toList();
    }
}
