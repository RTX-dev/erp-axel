package cc.lery.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DashController {

        @FXML
    private TextField fieldlName;

    @FXML
    private Text repeatName;

    @FXML
    public void BackToLogin(ActionEvent event)
    {
        try {
            //charger le fichier FXML de la deuxième page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home_page.fxml"));
            Parent root = loader.load();

            //Créer une nouvelle scène avec le contenu de la deuxième page
            Scene scene = new Scene(root);

            //Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            //En decomposant le code ci-dessus on pourrait écrire :
            //Node node = (Node) event.getSource();
            //Scene scene = node.getScene();
            //Stage stage = (Stage) scene.getWindow();

            //Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}