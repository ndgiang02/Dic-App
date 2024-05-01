package Controllers;

import Alerts.Alerts;
import CommandLine.Dictionary;
import CommandLine.DictionaryManagement;
import CommandLine.Word;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddWord extends Dictionary implements Initializable {
    private Dictionary dictionary = new Dictionary();
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final String path = "src/main/resources/data/dictionaries1.txt";
    private Alerts alerts = new Alerts();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictionaryManagement.insertFromFile(dictionary, path);
        if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) addBtn.setDisable(true);

        wordTargetInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) addBtn.setDisable(true);
                else addBtn.setDisable(false);
            }
        });

        explanationInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) addBtn.setDisable(true);
                else addBtn.setDisable(false);
            }
        });

        successalert.setVisible(false);
    }

    @FXML
    private void handleClickAddBtn() {
        Alert alertConfirmation = alerts.alertConfirmation("Add word", "Bạn chắc chắn muốn thêm từ này?");
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        String englishWord = wordTargetInput.getText().trim();
        String meaning = explanationInput.getText().trim();

        if (option.get() == ButtonType.OK) {
            Word word = new Word(englishWord, meaning);
            if (dictionary.contains(word)) {
                int indexOfWord = dictionaryManagement.searchWord(dictionary, englishWord);
                Alert selectionAlert = alerts.alertConfirmation("This word already exists", "Từ này đã tồn tại.\nThay thế hoặc bổ sung nghĩa vừa nhập cho nghĩa cũ.");
                selectionAlert.getButtonTypes().clear();
                ButtonType replaceBtn = new ButtonType("Thay thế");
                ButtonType insertBtn = new ButtonType("Bổ sung");
                selectionAlert.getButtonTypes().addAll(replaceBtn, insertBtn, ButtonType.CANCEL);
                Optional<ButtonType> selection = selectionAlert.showAndWait();

                if (selection.get() == replaceBtn) {
                    dictionary.get(indexOfWord).setWord_explain(meaning);
                    dictionaryManagement.dictionaryExportToFile(dictionary, path);
                    successAlert();
                }
                if (selection.get() == insertBtn) {
                    String oldMeaning = dictionary.get(indexOfWord).getWord_explain();
                    dictionary.get(indexOfWord).setWord_explain(oldMeaning + "\n= " + meaning);
                    dictionaryManagement.dictionaryExportToFile(dictionary, path);
                    successAlert();
                }
                if (selection.get() == ButtonType.CANCEL) alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
            } else {
                dictionary.add(word);
                dictionaryManagement.addWord(word, path);
                successAlert();
            }
            addBtn.setDisable(true);
            wordTargetInput.setText("");
            explanationInput.setText("");
        } else if (option.get() == ButtonType.CANCEL) alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
    }

    private void successAlert() {
        successalert.setVisible(true);
        setDelay(() -> successalert.setVisible(false), 2000);
    }

    private void setDelay(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    @FXML
    private Button addBtn;

    @FXML
    private TextArea wordTargetInput;

    @FXML
    private TextArea explanationInput;

    @FXML
    private Label successalert;
}
