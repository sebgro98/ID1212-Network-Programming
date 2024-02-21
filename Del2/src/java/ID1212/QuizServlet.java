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
@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quizId = request.getParameter("quizId");
        QuizModel quiz = DatabaseManager.getQuiz(quizId);

        // Set the QuizModel object in the session attribute instead of the request attribute
        request.getSession().setAttribute("quiz", quiz);

        // Redirect to the QuizResultServlet
        response.sendRedirect(request.getContextPath() + "/QuizResultServlet");
    }
}






