
package task3.Integration;

import java.sql.*;
import task3.Model.User;

public class UserDAO {
    String jdbcURL = "jdbc:mysql://localhost:3306/test";
    String dbUser = "root";
    String dbPassword = "root";
    public User checkLogin(String email, String username, String password) throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        String sql = "SELECT * FROM users WHERE username = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, password);
 
        ResultSet result = statement.executeQuery();

        User user = null;

        if (result.next()) {
            user = new User(result.getInt("id"));
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
        }
        connection.close();
        return user;
    }
}