package cc.lery.controller;

import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

public class MainController {
    @FXML
    private TextField filelName;

    @FXML
    private Text reapraName;

    @FXML
    private void handleButtonClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Button cliqué !");
        alert.showAndWait();
    }
}
