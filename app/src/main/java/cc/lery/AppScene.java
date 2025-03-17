package cc.lery;

    


import java.util.List;

import cc.lery.model.User;
import cc.lery.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppScene extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/home_page.fxml"));
        primaryStage.setTitle("ERP HP/AP");
        primaryStage.setScene(new Scene(root,1250, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        //URL de connexion à la base de données
        /*String url = "jdbc:mariadb://localhost:3306/erp";
        String user = "root";
        String password ="";

        Connection connexion = null;



        try {
            //Etablir la connéxion
            connexion = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de donnée MariaDB!");

            //Executer une requête
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, lastname FROM users");

            //Traiter les résultats
            while (resultSet.next()){
                System.out.println("ID: " + resultSet.getInt("Id"));
                System.out.println("Nom: " + resultSet.getString("Lastname"));
            }

            //Fermer les resources
            resultSet.close();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //fermer la connexion
            if (connexion != null){
                try {
                    connexion.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

    // appelle de DB en utilisant les UserService
        UserService userService = new UserService();

        List<User> listUser = userService.listAllUsers();
        for (int i = 0; i < listUser.size(); i++) {
            System.out.println("avec userService "+ listUser.get(i).getLastname() +" " + listUser.get(i).getId());
        }

        //Ajouter un User
        //userService.addUser("Thomas");

        //Suprimer un User
        //userService.deleteUser(5);

        //modifier un User
        //userService.updateUser(6,"Antoine");

        //sortir un User
        //System.out.println (userService.getUser(3).getLastname());
        
        launch(args);
    }
}
