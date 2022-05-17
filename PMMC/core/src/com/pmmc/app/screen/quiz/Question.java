package com.pmmc.app.screen.quiz;

import java.util.ArrayList;

public class Question {
    public String question;
    public String answer;
    public ArrayList<String> allChoices;

    public Question(String question, String answer, ArrayList<String> allChoices){
        this.question = question;
        this.answer = answer;
        this.allChoices = allChoices;
    }
}
