package ID1212;

/**
 *
 * @author Zebbe
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static Map<String, UserModel> users = new HashMap<>();
    private static Map<String, QuizModel> quizzes = new HashMap<>();

    static {
        // Add a sample user
        users.put("user@example.com", new UserModel("user@example.com", "password"));

        // Add a sample quiz
        QuizModel quiz = new QuizModel();
        quiz.addQuestion("What is the capital of France?", new String[]{"Paris", "Berlin", "Madrid"}, 0);
        quiz.addQuestion("What is the largest planet in our solar system?", new String[]{"Earth", "Jupiter", "Mars"}, 1);
        quiz.addQuestion("Who wrote 'Romeo and Juliet'?", new String[]{"William Shakespeare", "Jane Austen", "Charles Dickens"}, 0);
        quizzes.put("1", quiz);
    }

    public static UserModel getUser(String username) {
        return users.get(username);
    }

    public static void addUser(UserModel user) {
        users.put(user.getUsername(), user);
    }

    public static QuizModel getQuiz(String quizId) {
        return quizzes.get(quizId);
    }

    public static Map<String, QuizModel> getAllQuizzes() {
        return quizzes;
    }
}

