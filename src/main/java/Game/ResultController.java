package Game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ResultController implements Initializable {

    @FXML
    public Label remark, marks, markstext, correcttext, wrongtext;

    @FXML
    public ProgressIndicator correct_progress, wrong_progress;

    private int correct;
    private int wrong;
    private int sum;

    private void handleZeroSum() {
        System.err.println("Error: Không có câu hỏi nào được chọn. Hãy chạy lại chương trình.");
        System.exit(0);
    }

    @FXML
    public void handleBackwardButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DictionaryGUI.fxml"));
            Parent mainRoot = loader.load();
            Scene mainScene = new Scene(mainRoot);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Dictionary Apllication");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        correct = GameController.correct;
        wrong = GameController.wrong;
        sum = correct + wrong;

        //Nếu không nhập bất kì caau trả lời nào
        if (sum == 0) {
            handleZeroSum();
        }


        correcttext.setText("Correct Answers : " + correct);
        wrongtext.setText("Incorrect Answers : " + wrong);

        marks.setText(correct + "/" + sum);
        float correctf = (float) correct / sum;
        correct_progress.setProgress(correctf);

        float wrongf = (float) wrong / sum;
        wrong_progress.setProgress(wrongf);

        markstext.setText(correct + " Marks Scored");

        if (correctf <= 0.4) {
            remark.setText("Đừng nản lòng! Hãy ôn tập thêm và thử lại lần nữa! ");
        }  else if (correctf > 0.4 && correctf <= 0.8) {
            remark.setText("Làm tốt lắm! Bạn đã nắm bắt được khá nhiều kiến thức, cố gắng phát huy nhé! ");
        } else if (correctf >= 0.8 && correctf < 1) {
            remark.setText("Tuyệt vời! Bạn đã hiểu rất rõ về các chủ đề này. Tiếp tục duy trì phong độ nhé! ");
        } else if (correctf == 1) {
            remark.setText("Tuyệt vời! Chúc mừng bạn đã đạt điểm tuyệt đối! ");
        }
        //reset wrong và correct
        GameController.resetValues();
    }
}

