package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;


public class ResultController {

    @FXML
    private AnchorPane container;

    @FXML
    private ImageView home;

    @FXML
    public Label remark, marks, markstext, correcttext, wrongtext;

    @FXML
    public ProgressIndicator correct_progress, wrong_progress;

    private int correct;
    private int wrong;
    private int sum;

    @FXML
    private void initialize() {
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

    private void handleZeroSum() {
        System.err.println("Error: Không có câu hỏi nào được chọn. Hãy chạy lại chương trình.");
        System.exit(0);
    }

    @FXML
    public void handleBackwardButton(ActionEvent event) {
                container.getChildren().add(home);

    }
}
