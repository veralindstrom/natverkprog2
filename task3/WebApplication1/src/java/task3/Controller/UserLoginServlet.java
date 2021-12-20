
package task3.Controller;

import task3.Integration.QuizDAO;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import task3.Integration.UserDAO;
import task3.Model.User;
import java.util.List;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public UserLoginServlet() {
        super();
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
         
        UserDAO userDao = new UserDAO();
        QuizDAO quizDao = new QuizDAO();
        HttpSession session = request.getSession();
        try {
            /* the doPost() method handles the request to login from the client. 
            It calls the checkLogin() method of the UserDAO class to verify email and password against the database.*/
            User user = userDao.checkLogin(email, username, password);
            String destPage = "login.jsp";
            
            /*If the login succeeds, it sets an attribute in the session to store
            information about the logged in user, and forwards the request to the home page*/
            if (user != null) {
                session.setAttribute("user", user);
                List quizzes = quizDao.getQuizzes();
                if (quizzes != null) {
                    session.setAttribute("quizzes", quizzes);
                }
                destPage = "/score";
            } 
            /*If the login fails, sets error message as an attribute in the request, and forwards to the login page again*/
            else {
                String message = "Invalid username/password";
                request.setAttribute("message", message);
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
            dispatcher.forward(request, response);
             
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }

}
