package task3.Model;

public class User {
    private int id;
    private int score;
    private String email;
    private String username;
    private String password;
    
    public User(){
    }
    
    public User(int id){
        this.id = id;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public int getId(){
        return this.id;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public int getScore(){
        return this.score;
    }
}