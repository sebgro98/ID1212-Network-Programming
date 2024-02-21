package ID1212;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Zebbe
 */


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserModel user = DatabaseManager.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("username", username);
            response.sendRedirect("main.jsp");
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}


