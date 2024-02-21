package ID1212;

/**
 *
 * @author Zebbe
 */
import java.util.ArrayList;
import java.util.List;

public class QuizModel {
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(String text, String[] options, int correctOption) {
        questions.add(new Question(text, options, correctOption));
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static class Question {
        private String text;
        private String[] options;
        private int correctOption;

        public Question(String text, String[] options, int correctOption) {
            this.text = text;
            this.options = options;
            this.correctOption = correctOption;
        }

        public String getText() {
            return text;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectOption() {
            return correctOption;
        }
    }
}



