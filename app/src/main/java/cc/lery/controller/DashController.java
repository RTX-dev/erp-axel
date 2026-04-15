package cc.lery.controller;

import java.io.IOException;
import java.util.List;

import cc.lery.model.Sample;
import cc.lery.model.User;
import cc.lery.service.SampleService;
import cc.lery.service.UserService;
import cc.lery.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * DashController : Gère l'affichage du tableau de bord (Dashboard).
 * Affiche la liste des utilisateurs et gère les boutons d'admin.
 */
public class DashController {

    @FXML
    private TableView<User> userTable;           // Le tableau graphique
    @FXML
    private TableColumn<User, String> idColumn;  // Colonne pour l'ID
    @FXML
    private TableColumn<User, String> lastnameColumn; // Colonne pour le Nom
    
    @FXML
    private Button addUserBtn;    // Bouton "+" (Ajout utilisateur)
    @FXML
    private Button editUserBtn;   // Bouton "crayon" (Edition utilisateur)
    @FXML
    private Button addSampleBtn;  // Bouton "+" (Ajout prélèvement)

    @FXML
    private TableView<Sample> sampleTable;
    @FXML
    private TableColumn<Sample, String> receptionDateColumn;
    @FXML
    private TableColumn<Sample, String> namePatientColumn;

    /**
     * Initialisation : s'exécute dès que la page s'affiche.
     */
    @FXML
    public void initialize() {
        // --- SÉCURITÉ : GESTION DES RÔLES ---
        // Si l'utilisateur n'est pas ADMIN, on cache les boutons de modification.
        if (!SessionManager.isAdmin()) {
            addUserBtn.setVisible(false);
            editUserBtn.setVisible(false);
            
            // setManaged(false) permet de supprimer l'espace vide laissé par les boutons cachés.
            addUserBtn.setManaged(false);
            editUserBtn.setManaged(false);
        }

        // On dit à chaque colonne quelle donnée de l'objet 'User' elle doit afficher.
        // PropertyValueFactory("id") va chercher la méthode getId() dans User.java.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        // On remplit le tableau avec les données de la base.
        loadUsers();

        // Colonnes du tableau des prélèvements
        receptionDateColumn.setCellValueFactory(new PropertyValueFactory<>("receptionDate"));
        namePatientColumn.setCellValueFactory(new PropertyValueFactory<>("namePatient"));

        // On remplit le tableau des prélèvements
        loadSamples();
    }

    /**
     * Se déconnecte et revient à l'écran de login.
     */
    @FXML
    public void BackToLogin(MouseEvent event) {
        try {
            SessionManager.logout(); // On vide la session
            
            Parent root = FXMLLoader.load(getClass().getResource("/view/page-connexion.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la fenêtre pour ajouter un utilisateur.
     */
    @FXML
    private void AddUser(ActionEvent event) {
        ouvrirFenetreUtilisateur(null, "Nouvel utilisateur");
    }

    /**
     * Ouvre la modale pour ajouter un prélèvement.
     */
    @FXML
    private void AddSample(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addSamplePage.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Nouveau prélèvement");
            stage.initModality(Modality.APPLICATION_MODAL); // Bloque le dashboard derrière
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Attend la fermeture avant de continuer
            loadSamples(); // Rafraîchit le tableau après l'ajout
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible d'ouvrir la fenêtre : " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Ouvre la fenêtre pour modifier l'utilisateur sélectionné dans le tableau.
     */
    @FXML
    private void EditUser(ActionEvent event) {
        // On récupère la ligne sélectionnée dans le tableau
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            // Si rien n'est sélectionné, on affiche une alerte à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un utilisateur à modifier.");
            alert.showAndWait();
            return;
        }
        ouvrirFenetreUtilisateur(selectedUser, "Modifier utilisateur");
    }

    /**
     * Méthode générique pour ouvrir la petite fenêtre (modale) d'ajout/edition.
     */
    private void ouvrirFenetreUtilisateur(User user, String titre) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addUserPage.fxml"));
            Parent root = fxmlLoader.load();

            // On passe l'utilisateur au contrôleur de la petite fenêtre
            AddUserController controller = fxmlLoader.getController();
            controller.setUserToEdit(user);

            Stage stage = new Stage();
            stage.setTitle(titre);
            stage.initModality(Modality.APPLICATION_MODAL); // Empêche de cliquer sur le dashboard derrière
            stage.setScene(new Scene(root));
            stage.showAndWait(); // On attend que la fenêtre soit fermée pour continuer

            loadUsers(); // On rafraîchit la liste des utilisateurs à la fermeture
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge les utilisateurs depuis la base et rafraîchit le tableau.
     */
    public void loadUsers() {
        UserService userService = new UserService();
        List<User> users = userService.listAllUsers();
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList);
    }

    /**
     * Charge les prélèvements depuis la base et rafraîchit le tableau.
     */
    public void loadSamples() {
        SampleService sampleService = new SampleService();
        List<Sample> samples = sampleService.listAllSamples();
        ObservableList<Sample> sampleList = FXCollections.observableArrayList(samples);
        sampleTable.setItems(sampleList);
    }
}
