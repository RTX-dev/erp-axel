package cc.lery.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javafx.stage.Stage;

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
    
    @FXML
    private void handleOpenFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un ficher");

        //Définir un filtre pour n'afficher que certain types de fichier (ex: image)
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers texte(*.txt)","*.txt")
        );

        // Ouvrir la boîte de dialogue et récupérer le fichier sélectionné
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile !=null){
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        }else{
            System.out.println("Aucun ficher sélectionné");
        }
    }
}
