package cc.lery.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DashController {


    @FXML
    public void BackToLogin(MouseEvent event)
    {
        try {
            //charger le fichier FXML de la deuxième page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/page-connexion.fxml"));
            Parent root = loader.load();

            //Créer une nouvelle scène avec le contenu de la deuxième page
            Scene scene = new Scene(root);

            //Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            //Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}