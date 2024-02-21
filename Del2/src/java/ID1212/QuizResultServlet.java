package ID1212;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/QuizResultServlet")
public class QuizResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the quiz questions and correct answers from the session
        QuizModel quiz = (QuizModel) request.getSession().getAttribute("quiz");
        
        if (quiz != null) {
            List<QuizModel.Question> questions = quiz.getQuestions();
            
            // Calculate the quiz score
            int score = calculateScore(request, questions);

            // Display the results
            request.setAttribute("score", score);
            request.getRequestDispatcher("quizResult.jsp").forward(request, response);
        } else {
            // Handle the case where quiz is not found in the session
            response.getWriter().println("Quiz not found. Please start a new quiz.");
        }
    }

    private int calculateScore(HttpServletRequest request, List<QuizModel.Question> questions) {
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            // Get the user's answer for each question
            String answerParam = "answer_" + i;
            int userAnswer = Integer.parseInt(request.getParameter(answerParam));

            // Check if the user's answer is correct
            if (userAnswer == questions.get(i).getCorrectOption()) {
                score++;
            }
        }

        return score;
    }
}
