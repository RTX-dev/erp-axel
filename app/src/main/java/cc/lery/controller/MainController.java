package cc.lery.controller;

import cc.lery.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * MainController : Gère la première page de l'application (la connexion).
 */
public class MainController {

    // @FXML permet de lier les variables Java aux composants du fichier FXML.
    @FXML
    private TextField loginfield;     // Champ de saisie de l'email
    @FXML
    private PasswordField passwordfield; // Champ de saisie du mot de passe (caché)
    @FXML
    private Text errormsg;            // Texte pour afficher les erreurs de connexion

    /**
     * Méthode appelée quand l'utilisateur clique sur le bouton "Connexion".
     */
    @FXML
    private void handleConnectionButun(ActionEvent event){
        String email = loginfield.getText();
        String password = passwordfield.getText();
        
        UserService userService = new UserService();
        
        // On demande au service de vérifier si l'email et le mot de passe sont corrects
        if(userService.validateUser(email, password)){
            // SI CONNEXION RÉUSSIE : On charge le Dashboard
            chargerPage(event, "/view/dashboard.fxml");
        } else {
            // SI ÉCHEC : On affiche un message en rouge sur l'interface
            errormsg.setText("Le mot de passe ou le mail est incorrect");
        }
    }

    /**
     * Petite méthode utilitaire pour changer de fenêtre (Scene) facilement.
     */
    private void chargerPage(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // On récupère la fenêtre actuelle (Stage) à partir de l'événement du clic
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // On remplace le contenu de la fenêtre par la nouvelle page
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Erreur de chargement de la page " + fxmlPath + " : " + e.getMessage());
        }
    }

    /**
     * Méthode (optionnelle) pour aller directement au dashboard.
     */
    @FXML
    public void handleDashboard(ActionEvent event) {
        chargerPage(event, "/view/dashboard.fxml");
    }
}
