package com.example.quiz_api_management.question;

public enum QuestionFilter {
    MULTIPLE("multiple"),
    SHORT("short"),
    LONG("long");
    public final String questionType;
    QuestionFilter (String questionType){
        this.questionType = questionType;
    }

    public static boolean contains(String checkValid){
        for (QuestionFilter questionFilter: QuestionFilter.values()){
            System.out.println(questionFilter.questionType);
            if(questionFilter.questionType.equals(checkValid)) return true;
        }
        return false;
    }
}

