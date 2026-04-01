package cc.lery.controller;

import java.io.IOException;
import java.util.List;

import cc.lery.model.User;
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
 * Contrôleur du Dashboard principal.
 * Gère l'affichage de la liste des utilisateurs et les permissions d'accès aux fonctions admin.
 */
public class DashController {

    @FXML
    private TableView<User> userTable; // Le composant TableView affichant la liste des utilisateurs.
    @FXML
    private TableColumn<User, String> idColumn; // La colonne d'affichage de l'ID.
    @FXML
    private TableColumn<User, String> lastnameColumn; // La colonne d'affichage du nom de famille.
    @FXML
    private Button addUserBtn; // Bouton d'ajout d'un nouvel utilisateur (Admin uniquement).
    @FXML
    private Button editUserBtn; // Bouton d'édition d'un utilisateur existant (Admin uniquement).

    /**
     * Méthode d'initialisation appelée automatiquement par JavaFX lors du chargement de la vue.
     * Configure l'affichage de la table et gère les permissions d'affichage des boutons d'administration.
     */
    @FXML
    public void initialize() {
        UserService userService = new UserService();

        // --- GESTION DES PERMISSIONS ---
        // Vérifie si un utilisateur est connecté dans la session actuelle.
        if (SessionManager.isLogged()) {
            String role = SessionManager.getUser().getRole(); // Récupère le rôle de l'utilisateur.

            // Si le rôle n'est pas "ADMIN", les boutons d'ajout et d'édition sont masqués.
            if (!"ADMIN".equals(role)) {
                // Masque les boutons d'admin pour les utilisateurs non-admin (ex: MEDECIN).
                addUserBtn.setVisible(false);
                editUserBtn.setVisible(false);

                // setManaged(false) permet de retirer l'espace occupé par les boutons masqués dans l'interface.
                addUserBtn.setManaged(false);
                editUserBtn.setManaged(false);
            }
        }
        // Associe les colonnes de la TableView aux propriétés de l'objet User.
        // PropertyValueFactory("id") appelle getId() dans l'objet User.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        // Charge la liste des utilisateurs depuis la base de données.
        loadUsers();
    }
    /**
     * Retourne à l'écran de connexion et ferme la session.
     *
     * @param event L'événement de clic de la souris.
     */
    @FXML
    public void BackToLogin(MouseEvent event) {
        try {
            // Déconnexion de la session utilisateur actuelle.
            SessionManager.logout();

            // Charge la vue de connexion.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/page-connexion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene); // Affiche la vue de connexion.
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la fenêtre d'ajout d'un nouvel utilisateur.
     *
     * @param event L'événement de clic sur le bouton d'ajout.
     */
    @FXML
    private void AddUser(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addUserPage.fxml"));
            Parent root = fxmlLoader.load();

            AddUserController controller = fxmlLoader.getController();
            controller.setUserToEdit(null); // Informe le contrôleur qu'on est en mode ajout (pas d'utilisateur à modifier).

            Stage stage = new Stage();
            stage.setTitle("Nouvel utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL); // Bloque l'accès à la fenêtre parente (Dashboard) tant que la fenêtre modale est ouverte.
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Attend la fermeture de la fenêtre d'ajout.

            loadUsers(); // Recharge la liste des utilisateurs une fois l'ajout terminé.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la fenêtre d'édition pour l'utilisateur sélectionné dans la table.
     *
     * @param event L'événement de clic sur le bouton d'édition.
     */
    @FXML
    private void EditUser(ActionEvent event) {
        // Récupère l'utilisateur sélectionné dans la TableView.
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        // Si aucun utilisateur n'est sélectionné, affiche un message d'avertissement.
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun utilisateur sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un utilisateur à modifier.");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addUserPage.fxml"));
            Parent root = fxmlLoader.load();

            AddUserController controller = fxmlLoader.getController();
            controller.setUserToEdit(selectedUser); // Informe le contrôleur qu'on est en mode édition avec l'utilisateur sélectionné.

            Stage stage = new Stage();
            stage.setTitle("Modifier utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadUsers(); // Recharge la liste une fois la modification terminée.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère la liste des utilisateurs de la base de données et met à jour la TableView.
     */
    public void loadUsers() {
        UserService userService = new UserService();
        List<User> users = userService.listAllUsers();
        // Convertit la liste en une ObservableList nécessaire pour JavaFX.
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList); // Met à jour les éléments affichés par la TableView.
    }
}
