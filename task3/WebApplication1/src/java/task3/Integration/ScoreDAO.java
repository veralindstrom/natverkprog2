package task3.Integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ScoreDAO {
    String jdbcURL = "jdbc:mysql://localhost:3306/test";
    String dbUser = "root";
    String dbPassword = "root";
    
    public void setLatestScore(int userId, int quizId, int score) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "INSERT INTO results (user_id, quiz_id, score) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, quizId);
            statement.setInt(3, score);

            statement.executeUpdate();
        }    
        catch (SQLException ex){
            System.out.println(ex);
        }
        
    }
    
    public ArrayList<ArrayList<Integer>> getUserScores(int userId) throws SQLException, ClassNotFoundException {
        ArrayList<ArrayList<Integer>> scores = new ArrayList<>();
        ArrayList<Integer> scoreOneQuiz = new ArrayList<>();
 
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT  quiz_id, score FROM results WHERE user_id = ? ORDER BY quiz_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet result = statement.executeQuery();
            int quiz = 1;
            while (result.next()) {
                if(result.getInt("quiz_id") == quiz){
                    scoreOneQuiz.add(result.getInt("score"));
                }
                else {
                    quiz = result.getInt("quiz_id");
                    scores.add(scoreOneQuiz);
                    scoreOneQuiz = new ArrayList<>();
                }
            }

            if(!scoreOneQuiz.isEmpty())
                scores.add(scoreOneQuiz);
        }
        catch (SQLException ex){
            System.out.println(ex);
        }
        
        return scores;
    }
}
