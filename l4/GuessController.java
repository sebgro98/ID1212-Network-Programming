import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/GuessController")
public class GuessController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        // Hämta eller skapa en GuessBean från sessionen
        GuessBean guessBean = (GuessBean) session.getAttribute("guessBean");
        if (guessBean == null) {
            guessBean = new GuessBean();
            session.setAttribute("guessBean", guessBean);
        }

        // Hantera användarens gissning
        int userGuess = Integer.parseInt(request.getParameter("userGuess"));
        guessBean.setUserGuess(userGuess);

        // Skicka resultatet till JSP-sidan
        request.setAttribute("result", guessBean.getResult());
        request.getRequestDispatcher("/guess.jsp").forward(request, response);
    }
}
