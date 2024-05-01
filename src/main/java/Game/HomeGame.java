package Game;

import Controllers.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeGame extends Controller implements Initializable {

    @FXML
    private AnchorPane container;

    @FXML
    private Button playButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showComponent("/Views/GameGUI.fxml");
            }
        });
    }
}