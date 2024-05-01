package Controllers;


import Base.TranslateAPI;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslateController implements Initializable {

    private boolean isToVietnameseLang = true;

    private String sourceLang = "en", targetLang = "vi";

    public Label english, vietnam;

    public Button translateBtn, swapBtn, clearBtn;

    public TextArea translateTarget, translateExplain;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    onClicktranslateBtn();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        translateTarget.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (translateTarget.getText().trim().isEmpty()) {
                    translateBtn.setDisable(true);
                } else {
                    translateBtn.setDisable(false);
                }
            }
        });

        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                translateTarget.clear();
                translateBtn.setDisable(true);
            }
        });

        swapBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleOnClickSwap();
            }
        });
        translateBtn.setDisable(true);

    }

    @FXML
    public void onClicktranslateBtn() throws IOException {
        TranslateAPI translateAPI = new TranslateAPI();
        String target = translateTarget.getText();
        String explain = translateAPI.translate(sourceLang, targetLang, target);
        translateExplain.setText(explain);
    }

    @FXML
    private void handleOnClickSwap() {
        translateTarget.clear();
        translateExplain.clear();
        if (isToVietnameseLang) {
            vietnam.setText("Tiếng Việt");
            english.setText("Tiếng Anh");
            sourceLang = "vi";
            targetLang = "en";
        } else {
            vietnam.setText("Tiếng Anh");
            english.setText("Tiếng Việt");
            sourceLang = "en";
            targetLang = "vi";
        }
        isToVietnameseLang = !isToVietnameseLang;
    }
}
