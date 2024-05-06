package CommandLine;

import Base.Question;

import java.util.List;
import java.util.Scanner;


public class GameCommandLine extends DictionaryManagement {

    public static void MutilChoice() {

        List<Question> questions = readQuestionsFromFile(QUESTION_PATH);
        Scanner scanner = new Scanner(System.in);
        int score = 0;

        System.out.println("Welcome to the Multiple Choice Quiz Game!");
        System.out.println("There are " + questions.size() + " questions in total.");

        for (int i = 0; i < questions.size(); i++) {
            Question currentQuestion = questions.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + currentQuestion.getQuestion());
            List<String> options = currentQuestion.getOptions();

            // Display options
            for (int j = 0; j < options.size(); j++) {
                System.out.println((char) ('A' + j) + ". " + options.get(j));
            }

            // Get user's answer
            System.out.print("Your answer: ");
            char userAnswer = Character.toUpperCase(scanner.next().charAt(0)); // Đảm bảo lấy ký tự đầu tiên từ câu trả lời của người dùng và chuyển đổi thành chữ cái in hoa

            // Check if answer is correct
            if (userAnswer == currentQuestion.getCorrectAnswer().charAt(0)) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect! The correct answer is: " + currentQuestion.getCorrectAnswer().charAt(0));
            }
        }

        System.out.println("Your score is: " + score);
    }
}