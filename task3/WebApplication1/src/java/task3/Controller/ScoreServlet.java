package task3.Controller;

import task3.Integration.QuizDAO;
import task3.Integration.ScoreDAO;
import task3.Model.Quiz;
import task3.Model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/score")
public class ScoreServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        QuizDAO quizDao = new QuizDAO();
        ScoreDAO scoreDao = new ScoreDAO();

        try{
            Quiz quiz = quizDao.getQuiz(request.getParameter("chosenquiz"));
 
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if(user != null){
                    session.setAttribute("user", user);
                    ArrayList<ArrayList<Integer>> scores = scoreDao.getUserScores(user.getId());
                    session.setAttribute("scores", scores);
                }
                if(quiz != null){
                    session.setAttribute("quiz", quiz);
                }
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
            dispatcher.forward(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ScoreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    }
     
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        QuizDAO quizDao = new QuizDAO();
        ScoreDAO scoreDao = new ScoreDAO();

        try{
            Quiz quiz = quizDao.getQuiz(request.getParameter("chosenquiz"));
 
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if(user != null){
                    session.setAttribute("user", user);
                    ArrayList<ArrayList<Integer>> scores = scoreDao.getUserScores(user.getId());
                    session.setAttribute("scores", scores);
                }
                if(quiz != null){
                    session.setAttribute("quiz", quiz);
                }
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
            dispatcher.forward(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ScoreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    }
    
}
    