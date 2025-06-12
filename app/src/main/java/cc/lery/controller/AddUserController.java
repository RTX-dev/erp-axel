package cc.lery.controller;

import cc.lery.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {
    @FXML
    private TextField namefield;

    @FXML
    private TextField lastnamefield;
    
    @FXML
    private TextField mailfield;
    
    @FXML
    private TextField phoneNumberfield;
    
    @FXML
    private TextField passwordfield;

    @FXML
    private void handleAddUser(ActionEvent event){
        String name = namefield.getText();
        String lastname = lastnamefield.getText();
        String mail = mailfield.getText();
        String phoneNuber = phoneNumberfield.getText();
        String password = passwordfield.getText();

        UserService userService = new UserService();
        userService.addUser(name, lastname, mail, phoneNuber, password);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }
}
