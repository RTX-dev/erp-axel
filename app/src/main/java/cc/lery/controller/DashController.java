package cc.lery.controller;

import java.io.IOException;
import java.util.List;
import cc.lery.model.User;
import cc.lery.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TableColumn<User, String> lastnameColumn;

    @FXML
    public void initialize(){
        UserService userService = new UserService();
        //Associer les colonnes aux propriétés du modèle User
        //PropertyValueFactory cherche les méthodes du modèle basés sur le nom de la propriété préfixé de get (getId(), getLastname(),...)
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        //charger les données
        List<User> users = userService.listAllUsers();
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList);
    }


    @FXML
    public void BackToLogin(MouseEvent event)
    {
        try {
            //charger le fichier FXML de la deuxième page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/page-connexion.fxml"));
            Parent root = loader.load();

            //Créer une nouvelle scène avec le contenu de la deuxième page
            Scene scene = new Scene(root);

            //Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            //Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    // Méthode pour ouvrir la modale
    private void AddUser(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addUserPage.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Nouvelle utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL); // Modale
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Attend que la modale soit fermée
            loadUsers();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@FXML
    private void EditUser(ActionEvent event){
        try {

        }
    }*/

    public void loadUsers(){
        UserService  userDAO = new UserService();
        List<User> users = userDAO.listAllUsers();
        userTable.getItems().clear();
        userTable.getItems().addAll(users);
    }


}