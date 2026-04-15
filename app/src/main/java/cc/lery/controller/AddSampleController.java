package cc.lery.controller;

import cc.lery.service.SampleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * AddSampleController : Gère la fenêtre d'ajout d'un prélèvement.
 * C'est ce controller qui est lié au fichier addSamplePage.fxml.
 * Il récupère les données du formulaire et les envoie au SampleService pour les enregistrer en DB.
 */
public class AddSampleController {
    // JavaFX remplit automatiquement ces variables à l'ouverture de la fenêtre.
    @FXML
    private TextField nameField;

    @FXML
    private TextField namePatientField;

    @FXML
    private DatePicker receptionDatePicker;

    @FXML
    private TextArea descriptionField;

    // On instancie le service qui va gérer la logique métier et l'accès à la base de données.
    private SampleService sampleService = new SampleService();

    /**
     * Appelé automatiquement quand on clique sur le bouton "Ajouter" (onAction="#handleAction" dans le FXML).
     * @param event contient des infos sur le clic (quelle fenêtre, quel bouton, etc.)
     */
    @FXML
    private void handleAction(ActionEvent event) {

        // Récupérer les valeurs saisies dans le formulaire ---
        String name = nameField.getText();           // getText() retourne ce que l'utilisateur a tapé
        String namePatient = namePatientField.getText();
        String description = descriptionField.getText();
        // On ne le stocke pas dans une variable ici, on l'utilise directement plus bas

        // Vérifier que les champs obligatoires sont remplis
        if (name.isEmpty() || namePatient.isEmpty() || receptionDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setContentText("Veuillez remplir le nom, le nom du patient et la date de réception.");
            alert.showAndWait();
            return;
        }

        // Envoyer les données au service pour les enregistrer en DB
        long newId = sampleService.addSample(name, description, receptionDatePicker.getValue(), namePatient);
        //long = int mais en bcp plus gros car BIGINT dans ma db

        if (newId == -1) {
            // L'insertion a échoué
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue lors de l'enregistrement. Vérifiez la connexion à la base de données.");
            alert.showAndWait();
            return;
        }

        // Confirmation de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("Prélèvement ajouté avec succès !");
        alert.showAndWait();

        // Fermer la modale
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}