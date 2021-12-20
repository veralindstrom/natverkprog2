package task3.Integration;

import task3.Model.Question;
import task3.Model.Quiz;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    String jdbcURL = "jdbc:mysql://localhost:3306/test";
    String dbUser = "root";
    String dbPassword = "root";
        
    public List getQuizzes() throws ClassNotFoundException, SQLException{
        List quizzes = new ArrayList();
 
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT * FROM quizzes";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery();
            ResultSet result = statement.getResultSet();
            
            Quiz quiz = null;
            
            while (result.next()) {
                quizzes.add(result.getString("subject"));
                quiz = new Quiz(result.getInt("id"));
                quiz.setSubject(result.getString("subject"));
            }
        }
        
        return quizzes;
    }
    
    public Quiz getQuiz(String subject) throws ClassNotFoundException, SQLException{
        Quiz quiz = null;
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT * FROM quizzes WHERE subject = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, subject);
            statement.executeQuery();
            ResultSet result = statement.executeQuery();
            
            while (result.next()) {
                quiz = new Quiz(result.getInt("id"));
                quiz.setSubject(result.getString("subject"));
            }
        }
        return quiz;
    }
    
     public ArrayList<Question> getQuestions(int quizId) throws ClassNotFoundException, SQLException{
        ArrayList<Question> questions = new ArrayList(); 
 
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT questions.text, questions.`options`, questions.answer\n" +
                         "FROM selector\n" +
                         "INNER JOIN questions ON questions.id = selector.question_id\n" +
                         "WHERE quiz_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizId);
            statement.executeQuery();
            ResultSet result = statement.executeQuery();
    
            Question question = null;
            while (result.next()) {
                String[] options = result.getString("options").split("/");
                String[] a = result.getString("answer").split("/");
                
                int numberOfCorrectAns = 0;
                ArrayList<Integer> indexOfCorrectAns = new ArrayList<>();
                for(int i = 0; i < a.length; i++){
                    if(Integer.parseInt(a[i]) == 1){
                        indexOfCorrectAns.add(i);
                        numberOfCorrectAns = numberOfCorrectAns + 1;
                    }
                }
                String text = result.getString("text");
 
                question = new Question(text, options, indexOfCorrectAns, numberOfCorrectAns);
                questions.add(question);
            } 
        }
        return questions;
    }
}
