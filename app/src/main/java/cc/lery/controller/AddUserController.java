package cc.lery.controller;

import cc.lery.model.User;
import cc.lery.model.Role;
import cc.lery.service.RoleService;
import cc.lery.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

/**
 * AddUserController : Gère la fenêtre d'ajout ou de modification d'un utilisateur.
 */
public class AddUserController {
    
    // Champs de texte du formulaire liés au fichier FXML
    @FXML
    private TextField firstnamefield;
    @FXML
    private TextField lastnamefield;
    @FXML
    private TextField mailfield;
    @FXML
    private TextField phoneNumberfield;
    @FXML
    private TextField passwordfield;
    
    // La liste graphique pour sélectionner les rôles
    @FXML
    private ListView<Role> roleListView;
    
    @FXML
    private Button actionButton; // Bouton "Ajouter" ou "Modifier"
    @FXML
    private Text titleText;      // Titre de la fenêtre

    private User userToEdit;     // Garde en mémoire si on est en train de modifier quelqu'un
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();

    /**
     * S'exécute à l'ouverture de la fenêtre.
     */
    @FXML
    public void initialize() {
        // 1. Charger tous les rôles existants depuis la base de données
        ObservableList<Role> roles = FXCollections.observableArrayList(roleService.getAllRoles());
        roleListView.setItems(roles);

        // 2. Activer la sélection MULTIPLE (Maintenir CTRL ou SHIFT pour plusieurs)
        roleListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // 3. Configurer l'affichage : on veut voir le 'nameFunction' du rôle dans la liste
        roleListView.setCellFactory(lv -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) {
                    setText(null);
                } else {
                    setText(role.getNameFunction());
                }
            }
        });
    }

    /**
     * Appelé quand on clique sur le bouton de validation (Ajouter/Modifier).
     */
    @FXML
    private void handleAction(ActionEvent event) {
        // On récupère les textes saisis
        String firstname = firstnamefield.getText();
        String lastname = lastnamefield.getText();
        String mail = mailfield.getText();
        String phoneNumber = phoneNumberfield.getText();
        String password = passwordfield.getText();
        
        // On récupère TOUS les rôles cochés dans la liste
        List<Role> selectedRoles = roleListView.getSelectionModel().getSelectedItems();

        if (userToEdit == null) {
            // --- CAS AJOUT ---
            int newUserId = userService.addUser(lastname, firstname, mail, phoneNumber, password);
            
            if (newUserId != -1 && !selectedRoles.isEmpty()) {
                // Pour chaque rôle sélectionné, on crée un lien dans la table 'Assign'
                for (Role role : selectedRoles) {
                    roleService.assignRoleToUser(newUserId, role.getIdRole());
                }
            }
        } else {
            // --- CAS MODIFICATION ---
            userService.updateUser(userToEdit.getId(), lastname, firstname, mail, phoneNumber, password);
        }

        // On ferme la petite fenêtre après l'action
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Permet au Dashboard de dire à cette fenêtre : 
     * "Tiens, voici l'utilisateur à modifier" (ou null si c'est un ajout).
     */
    public void setUserToEdit(User user) {
        this.userToEdit = user;
        
        if (user != null) {
            // Mode EDITION : On remplit les champs avec les données actuelles
            firstnamefield.setText(user.getFirstname());
            lastnamefield.setText(user.getLastname());
            mailfield.setText(user.getMail());
            phoneNumberfield.setText(user.getPhoneNumber());
            passwordfield.setText(user.getPassword());

            actionButton.setText("Modifier");
            titleText.setText("Modifier utilisateur");
            roleListView.setDisable(true); // On désactive le changement de rôles en édition pour rester simple
        } else {
            // Mode AJOUT : Formulaire vide
            actionButton.setText("Ajouter");
            titleText.setText("Nouvel utilisateur");
            roleListView.setDisable(false);
        }
    }
}
