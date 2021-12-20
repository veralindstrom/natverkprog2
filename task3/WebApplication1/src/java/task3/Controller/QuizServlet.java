package task3.Controller;

import task3.Integration.ScoreDAO;
import task3.Integration.QuizDAO;
import task3.Model.Quiz;
import task3.Model.User;
import task3.Model.Question;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        QuizDAO quizDao = new QuizDAO();
        try {     
            Quiz quiz = quizDao.getQuiz(request.getParameter("chosenquiz"));
            ArrayList<Question> questions = quizDao.getQuestions(quiz.getId());
            String[] questionTexts = new String[questions.size()];
            ArrayList<String[]> questionOptions = new ArrayList<>();
            
            if (session != null) {
                User user = (User) session.getAttribute("user");
                
                if(user != null){
                    session.setAttribute("user", user);
                    session.setAttribute("quiz", quiz);
                }
                
                for(int i = 0; i < questions.size(); i++){
                    questionTexts[i] = questions.get(i).getText();
                    questionOptions.add(questions.get(i).getOptions());
                }

                if(questionTexts != null){
                    session.setAttribute("questionTexts", questionTexts);
                }
                
                if(!questionOptions.isEmpty()){
                    session.setAttribute("questionOptions", questionOptions);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("quiz.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(QuizServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        QuizDAO quizDao = new QuizDAO();
        ScoreDAO scoreDao = new ScoreDAO();
        HttpSession session = request.getSession(false);
        int score = 0;
        try {
            Quiz quiz = (Quiz) session.getAttribute("quiz");
            ArrayList<Question> questions = quizDao.getQuestions(quiz.getId());
            ArrayList<Integer> answeredIndexes = new ArrayList<>();
            User user = (User) session.getAttribute("user");
            
                    
            for (int i = 0; i < questions.size(); i++){ 
                int numberOfOptions = questions.get(i).getNumberOfOptions();
                answeredIndexes.clear();
                for (int j = 0; j < numberOfOptions; j++){
                    if (request.getParameter("check" + i + "." + j) != null){
                        int ans = Integer.parseInt(request.getParameter("check" + i + "." + j));
                        answeredIndexes.add(ans);
                    } 
                }
                
                if(questions.get(i).checkResult(answeredIndexes)){
                    score++;
                    session.setAttribute("correct" + i, "Correct");
                } else {
                    session.setAttribute("correct" + i, "Wrong");
                }
            }
            quiz.setLatestScore(score);
            scoreDao.setLatestScore(user.getId(), quiz.getId(), score);
            session.setAttribute("score", score);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
            dispatcher.forward(request, response);
             
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }
}