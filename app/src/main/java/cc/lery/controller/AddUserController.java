package cc.lery.controller;

import cc.lery.model.User;
import cc.lery.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private Button actionButton;
    @FXML
    private Text titleText;

    private User userToEdit;
    private UserService userService = new UserService();  // initialisation du service

@FXML
private void handleAction(ActionEvent event) {
    // Cette méthode remplace handleAddUser, car elle gère ajout ET modification
    String firstname = firstnamefield.getText();
    String lastname = lastnamefield.getText();
    String mail = mailfield.getText();
    String phoneNumber = phoneNumberfield.getText();
    String password = passwordfield.getText();

    if (userToEdit == null) {
        userService.addUser(lastname, firstname, mail, phoneNumber, password);
    } else {
        userService.updateUser(userToEdit.getId(), lastname, firstname, mail, phoneNumber, password);
    }

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
}

public void setUserToEdit(User user) {
    this.userToEdit = user;
    if (user != null) {
        // Remplir les champs
        firstnamefield.setText(user.getFirstname());
        lastnamefield.setText(user.getLastname());
        mailfield.setText(user.getMail());
        phoneNumberfield.setText(user.getPhoneNumber());
        passwordfield.setText(user.getPassword());

        // Modifier le texte du bouton et du titre
        actionButton.setText("Modifier");
        titleText.setText("Modifier utilisateur");
    } else {
        actionButton.setText("Ajouter");
        titleText.setText("Nouvel utilisateur");
    }
}

}
