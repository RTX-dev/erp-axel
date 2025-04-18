package cc.lery.controller;

import java.io.IOException;

import cc.lery.model.User;
import cc.lery.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TextField loginfield;
    @FXML
    private TextField passwordfield;
    @FXML
    private Text texterror;

    @FXML
    private Text repeatName;


    /*@FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un ficher");

        //Définir un filtre pour n'afficher que certain types de fichier (ex: image)
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers texte(*.txt)", "*.txt")
        );

        // Ouvrir la boîte de dialogue et récupérer le fichier sélectionné
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Aucun ficher sélectionné");
        }
    }*/
    @FXML
    public void handleConnectionButun(ActionEvent event) {
        UserService userService = new UserService();
        try {
            String mail = loginfield.getText();
            String plainpassword = passwordfield.getText();
            //String texTerror = texterror.setText(0);
            User user = userService.getUserByMail(mail);
            if (user == null) {
                //Message d'erreur
                System.out.println("Le mot de passse ou le mail est incorrect");
            } else {
                Boolean result = user.checkpassword(plainpassword, user.getPassword());
                if (result == true) {
                    //affichage page Dashboard
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                    Parent root = loader.load();

                    //Créer une nouvelle scène avec le contenu de la deuxième page
                    Scene scene = new Scene(root);

                    //Obtenir la scène actuelle à partir de l'événement
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    //En decomposant le code ci-dessus on pourrait écrire :
                    //Node node = (Node) event.getSource();
                    //Scene scene = node.getScene();
                    //Stage stage = (Stage) scene.getWindow();

                    //Définir la nouvelle scène sur le stage
                    stage.setScene(scene);
                    stage.show();
                } else {
                    //Message d'erreur
                    System.out.println("Le mot de passse ou le mail est incorrect");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*@FXML
    public void handleDashboard(ActionEvent event) {
        try {
            //affichage page Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent root = loader.load();

            //Créer une nouvelle scène avec le contenu de la deuxième page
            Scene scene = new Scene(root);

            //Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    }*/
}
