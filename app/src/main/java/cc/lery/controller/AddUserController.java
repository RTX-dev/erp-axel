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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AddUserController {
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
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private Button actionButton;
    @FXML
    private Text titleText;

    private User userToEdit;
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();

    @FXML
    public void initialize() {
        // Charger les rôles depuis la DB
        ObservableList<Role> roles = FXCollections.observableArrayList(roleService.getAllRoles());
        roleComboBox.setItems(roles);

        // Configurer l'affichage du nom du rôle dans la ComboBox
        roleComboBox.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return role == null ? "" : role.getNameFunction();
            }

            @Override
            public Role fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleAction(ActionEvent event) {
        String firstname = firstnamefield.getText();
        String lastname = lastnamefield.getText();
        String mail = mailfield.getText();
        String phoneNumber = phoneNumberfield.getText();
        String password = passwordfield.getText();
        Role selectedRole = roleComboBox.getSelectionModel().getSelectedItem();

        if (userToEdit == null) {
            // AJOUT
            int newUserId = userService.addUser(lastname, firstname, mail, phoneNumber, password);
            if (newUserId != -1 && selectedRole != null) {
                // Assigner le rôle dans la table de jointure Assign
                roleService.assignRoleToUser(newUserId, selectedRole.getIdRole());
            }
        } else {
            // MODIFICATION (le rôle n'est pas géré ici pour le moment)
            userService.updateUser(userToEdit.getId(), lastname, firstname, mail, phoneNumber, password);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setUserToEdit(User user) {
        this.userToEdit = user;
        if (user != null) {
            firstnamefield.setText(user.getFirstname());
            lastnamefield.setText(user.getLastname());
            mailfield.setText(user.getMail());
            phoneNumberfield.setText(user.getPhoneNumber());
            passwordfield.setText(user.getPassword());

            actionButton.setText("Modifier");
            titleText.setText("Modifier utilisateur");
            roleComboBox.setDisable(true); // Désactiver le changement de rôle en édition pour le moment
        } else {
            actionButton.setText("Ajouter");
            titleText.setText("Nouvel utilisateur");
            roleComboBox.setDisable(false);
        }
    }
}
