package task3.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Question {

    private final String text;
    private final String[] options;
    private final ArrayList<Integer> correct;
    private final int numberOfCorrect;
    
    public Question(String text, String[] options, ArrayList<Integer> correct, int numberOfCorrect){
        this.text = text;
        this.options = options;
        this.correct = correct;
        this.numberOfCorrect = numberOfCorrect;
    }
    
    public String getText(){
        return this.text;
    }
    
    public String[] getOptions(){
        return this.options;
    }
    
    public ArrayList<Integer> getCorrect(){
        return this.correct;
    }
    
    public int getNumberOfCorrect(){
        return this.numberOfCorrect;
    }
    
    public int getNumberOfOptions(){
        int count = 0;
        for (String option : this.options) {
            count = count + 1;
        }
        return count;
    }
    
    public boolean checkResult(ArrayList<Integer> answers){
        int count = 0;
        for (Integer ans : answers){ 
            if (checkResult(ans))
                count = count + 1;
        }
        return count == this.numberOfCorrect;
    }
    
    public boolean checkResult(Integer answer){
        for (Integer cor : this.correct) { 
            if (answer.equals(cor))
                return true;
        }
        return false;
    }
}
