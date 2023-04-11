package com.example.quiz_api_management.quiz;

public enum QuizQuery {
    TYPE("type"),
    COUNT("count");

    public final String quizProperty;

    QuizQuery(String quizProperty){
        this.quizProperty = quizProperty;
    }

    public static boolean contains (String checkValid){
        for (QuizQuery quizFilter: QuizQuery.values()){
            if(quizFilter.quizProperty.equals(checkValid)) return true;
        }
        return false;
    }
}
