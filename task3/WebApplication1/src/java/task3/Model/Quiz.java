package task3.Model;

public class Quiz {
    String subject;
    int id;
    int latestScore;
    
    public Quiz(int id){
        this.id = id;
    }
    
    public void setLatestScore(int score){
        this.latestScore = score;
    }
    
    public int getLatestScore(){
        return this.latestScore;
    }
    
    public int getId(){
        return this.id;
    }
    
    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getSubject(){
        return this.subject;
    } 
}
