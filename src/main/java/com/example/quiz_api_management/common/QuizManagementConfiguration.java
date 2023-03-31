
package com.example.quiz_api_management.common;

import com.example.quiz_api_management.answer.Answer;
import com.example.quiz_api_management.answer.AnswerRepository;
import com.example.quiz_api_management.question.Question;
import com.example.quiz_api_management.question.QuestionRepository;
import com.example.quiz_api_management.quiz.Quiz;
import com.example.quiz_api_management.quiz.QuizRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class QuizManagementConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(QuestionRepository questionRepository, AnswerRepository answerRepository, QuizRepository quizRepository) {
        return args -> {

            Quiz firstQuiz = new Quiz("Math 1");
            Question firstQuestion = new Question("Equation(s) to get result of 4 ?", "multiple", firstQuiz);
            Answer firstAnswer = new Answer("2 * 2", true, firstQuestion);

            Answer secondAnswer = new Answer("2 + 2", true, firstQuestion);
            Answer thirdAnswer = new Answer("2 / 2", false, firstQuestion);
            Answer fourthAnswer = new Answer("2 - 2", false, firstQuestion);

            Question secondQuestion = new Question("2 + 6 is ?", "short",firstQuiz);
            Answer fifthAnswer = new Answer("8", true, secondQuestion);

            Question thirdQuestion = new Question("Given any number, what is the result after multiplying by 1 ?", "long", firstQuiz);
            Answer sixthAnswer = new Answer("Itself", true, thirdQuestion);

            Question fourthQuestion = new Question("Is zero an even number ?", "long", firstQuiz);
            Answer seventhAnswer = new Answer("Yes", true, fourthQuestion);

            Question fifthQuestion = new Question("Is zero a natural number ?", "long", firstQuiz);
            Answer eighthAnswer = new Answer("Yes", true, fifthQuestion);
            // second_question.setAnswers(List.of(fifth_answer));

            List<Question> questions = new ArrayList<>(List.of(firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQuestion));
            List<Answer> answers = new ArrayList<>(List.of(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, fifthAnswer, sixthAnswer, seventhAnswer, eighthAnswer));



            Quiz secondQuiz = new Quiz("Math 2");
            Question sixthQuestion = new Question("a*a ?", "multiple", secondQuiz);
            Answer ninthAnswer = new Answer("Power of 2", true, sixthQuestion);

            Question seventhQuestion = new Question("2 to the power of 3 ?", "short", secondQuiz);
            Answer tenthAnswer = new Answer("8", true, seventhQuestion);

            Question eighthQuestion = new Question("Square root of 1 ?", "short", secondQuiz);
            Answer eleventhAnswer = new Answer("1", true, eighthQuestion);

            Question ninthQuestion = new Question("Given any number, the 1st power of that number is ?", "long", secondQuiz);
            Answer twelfthAnswer = new Answer("Itself", true, ninthQuestion);

            Question tenthQuestion = new Question("2^2 equals to ?", "multiple", secondQuiz);
            Answer thirteenthAnswer = new Answer("4", true, tenthQuestion);
            Answer fourteenthAnswer = new Answer("8", false, tenthQuestion);
            Answer fifteenthAnswer = new Answer("16", false, tenthQuestion);
            Answer sixteenthAnswer = new Answer("2 * 2", true, tenthQuestion);
            questions.addAll(List.of(sixthQuestion, seventhQuestion, eighthQuestion, ninthQuestion, tenthQuestion));
            answers.addAll(List.of(ninthAnswer, tenthAnswer, eleventhAnswer, twelfthAnswer, thirteenthAnswer, fourteenthAnswer, fifteenthAnswer, sixteenthAnswer));



            Quiz thirdQuiz = new Quiz("Basic Java 1");
            Question eleventhQuestion = new Question("WORA is ?", "long", thirdQuiz);
            Answer seventeenthAnswer = new Answer("Write Once Run Anywhere", true, eleventhQuestion);

            Question twelfthQuestion = new Question("Is Java object-oriented programming language ?", "long", thirdQuiz);
            Answer eighteenthAnswer = new Answer("Yes", true, twelfthQuestion);

            Question thirteenthQuestion = new Question("How many primitive(s) are there ?", "multiple", thirdQuiz);
            Answer nineteenthAnswer = new Answer("5", false, thirteenthQuestion);
            Answer twentiethAnswer = new Answer("6", true, thirteenthQuestion);
            Answer twentyFirstAnswer = new Answer("7", true, thirteenthQuestion);
            Answer twentySecondAnswer = new Answer("8", true, thirteenthQuestion);

            Question fourthteenQuestion = new Question("Is Java object-oriented programming language ?", "long", thirdQuiz);
            Answer twentyThirdAnswer = new Answer("Yes", true, fourthteenQuestion);

            Question fifteenthQuestion = new Question("What is a class ?","long", thirdQuiz);
            Answer twentyFourthAnswer = new Answer("A blueprint of object", true, fifteenthQuestion);

            questions.addAll(List.of(eleventhQuestion, twelfthQuestion, thirteenthQuestion, fourthteenQuestion, fifteenthQuestion));
            answers.addAll(List.of(seventeenthAnswer, eighteenthAnswer, nineteenthAnswer, twentiethAnswer, twentyFirstAnswer, twentySecondAnswer, twentyThirdAnswer, twentyFourthAnswer));



            Quiz fourthQuiz = new Quiz("Basic Java 2");
            Question sixteenthQuestion = new Question("How to define main function in Java ?", "long", fourthQuiz);
            Answer twentyFifthAnswer = new Answer("public static void main", true, sixteenthQuestion);

            Question seventeenthQuestion = new Question("How many byte(s) does long type have?", "multiple", fourthQuiz);
            Answer twentySixthAnswer = new Answer("2", false, seventeenthQuestion);
            Answer twentySeventhAnswer = new Answer("4", false, seventeenthQuestion);
            Answer twentyEightAnswer = new Answer("6", false, seventeenthQuestion);
            Answer twentyNinthAnswer = new Answer("8", true, seventeenthQuestion);

            Question eighteenthQuestion = new Question("Most keyword to use interface ?", "long", fourthQuiz);
            Answer thirdtiethAnswer = new Answer("implements",true, eighteenthQuestion);

            Question nineteenthQuestion = new Question("What is JVM ?", "long", fourthQuiz);
            Answer thirtyFirstAnswer = new Answer("Java Virtual Machine", true, nineteenthQuestion);

            Question twentiethQuestion = new Question("Keyword to use abstract class ?", "long", fourthQuiz);
            Answer thirdtySecondAnswer = new Answer("extends", true, twentiethQuestion);

            questions.addAll(List.of(sixteenthQuestion, seventeenthQuestion, eighteenthQuestion, nineteenthQuestion, twentiethQuestion));
            answers.addAll(List.of(twentyFifthAnswer, twentySixthAnswer, twentySeventhAnswer, twentyEightAnswer, twentyNinthAnswer, thirdtiethAnswer, thirtyFirstAnswer, thirdtySecondAnswer));



            Quiz fifthQuiz = new Quiz("Java OOP");
            Question twentyFirstQuestion = new Question("How to provoke superclass ?", "long", fifthQuiz);
            Answer thirtyThirdAnswer = new Answer("super", true, twentyFirstQuestion);

            Question twentySecondQuestion = new Question("How many principle(s) does OOP have?", "multiple", fifthQuiz);
            Answer thirtyFourthAnswer = new Answer("2", false, twentySecondQuestion);
            Answer thirtyFifthAnswer = new Answer("3", false, twentySecondQuestion);
            Answer thirtySixthAnswer = new Answer("4", true, twentySecondQuestion);
            Answer thirtySeventhAnswer = new Answer("5", false, twentySecondQuestion);

            Question twentyThirdQuestion = new Question("What is encapsulation ?", "long", fifthQuiz);
            Answer thirtyEightAnswer = new Answer("A process of wrapping code and data together into a single unit", true, twentyThirdQuestion);

            Question twentyFourthQuestion = new Question("What is abstraction ?", "long", fifthQuiz);
            Answer thirtyNinthAnswer= new Answer("A process of hiding the implementation detail", true, twentyFourthQuestion);

            Question twentyFifthQuestion = new Question("How many ways to achieve abstraction ?", "short", fifthQuiz);
            Answer fourtiethAnswer = new Answer("2", true, twentyFifthQuestion);

            questions.addAll(List.of(twentyFirstQuestion, twentySecondQuestion, twentyThirdQuestion, twentyFourthQuestion, twentyFifthQuestion));
            answers.addAll(List.of(thirtyThirdAnswer, thirtyFourthAnswer, thirtyFifthAnswer, thirtySixthAnswer, thirtySeventhAnswer, thirtyEightAnswer, thirtyNinthAnswer, fourtiethAnswer));


            quizRepository.saveAll(List.of(firstQuiz, secondQuiz, thirdQuiz, fourthQuiz, fifthQuiz));
            questionRepository.saveAll(questions);
            answerRepository.saveAll(answers);
        };

    }
}


