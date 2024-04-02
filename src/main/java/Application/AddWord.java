package Application;

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
import java.util.concurrent.Delayed;

public class AddWord implements Initializable {

    private Dictionary dictionary = new Dictionary();

    private DictionaryManagement dictionaryManagement = DictionaryManagement.getInstance();

    String path = "src/main/resources/Vocab/dictionaries1.txt";

    public TextArea addtarget, addexplain;
    public Button addbtn;

    public Label successalert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (addtarget.getText().isEmpty() || addexplain.getText().isEmpty()) {
            addbtn.setDisable(true);
        }
        addtarget.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (addtarget.getText().isEmpty() || addexplain.getText().isEmpty()) {
                    addbtn.setDisable(true);
                } else {
                    addbtn.setDisable(false);
                }
            }
        });

        addexplain.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (addtarget.getText().isEmpty() || addexplain.getText().isEmpty()) {
                    addbtn.setDisable(true);
                } else {
                    addbtn.setDisable(false);
                }
            }
        });
        successalert.setVisible(false);
    }

    @FXML
    public void handleaddbutton() throws InterruptedException {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Add Word");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn thêm từ này?");
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Huỷ", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(ok, ButtonType.CANCEL);
        Optional<ButtonType> option = confirm.showAndWait();
        String target = addtarget.getText().trim();
        String explain = addexplain.getText().trim();

        if (option.get() == ButtonType.OK) {
            Word word = new Word(target, explain);
            if (dictionary.contains(word)) {
                int indexOfWord = dictionaryManagement.searchWord(dictionary, target);
                Alert select = new Alert(Alert.AlertType.WARNING);
                select.setTitle("Warning!");
                select.setHeaderText(null);
                select.setContentText("Từ đã có sẵn!\nBạn có thể chọn bổ sung hoặc thay thế nghĩa của từ đã nhập");
                ButtonType replaceExplain = new ButtonType("Replace");
                ButtonType addExplain = new ButtonType("Add");
                select.getButtonTypes().setAll(replaceExplain, addExplain, ButtonType.CANCEL);
                Optional<ButtonType> optional1 = select.showAndWait();

                if (optional1.get() == replaceExplain) {
                    dictionary.get(indexOfWord).setWord_explain(explain);
                    dictionaryManagement.dictionaryExportToFile();
                    successalert();
                }
                if (optional1.get() == addExplain) {
                    String oldMeaning = dictionary.get(indexOfWord).getWord_explain();
                    dictionary.get(indexOfWord).setWord_explain(oldMeaning + "\n= " + explain);
                    dictionaryManagement.dictionaryExportToFile();
                    successalert();
                }
                else {
                dictionary.add(word);
                dictionaryManagement.addWord(target, explain);
                successalert();
            }

        } else if (option.get() == ButtonType.CANCEL){
            Alert end = new Alert(Alert.AlertType.INFORMATION);
            end.setTitle(null);
            end.setHeaderText(null);
            end.setContentText("Thay đổi không được lưu!");
            }
            addbtn.setDisable(true);
            addtarget.setText("");
            addexplain.setText("");
        }
    }

    private void successalert() {
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
}
