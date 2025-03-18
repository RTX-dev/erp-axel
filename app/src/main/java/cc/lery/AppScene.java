package cc.lery;

import cc.lery.model.User;
import cc.lery.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppScene extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/page-connexion.fxml"));
        primaryStage.setTitle("ERP HP/AP");
        primaryStage.setScene(new Scene(root, 640, 400));
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

        //Sortir tout les user
        /*List<User> listUser = userService.listAllUsers();
        for (int i = 0; i < listUser.size(); i++) {
            System.out.println("avec userService "+ listUser.get(i).getLastname() +" " + listUser.get(i).getId());
        }*/
        //sortir un User
        //System.out.println (userService.getUserId(3).getLastname());
        //Ajouter un User
        //userService.addUser("Turière","Thomas","thoma@gmail.com","+33 1 23 45 67 89", "gngngng");
        //Suprimer un User
        //userService.deleteUser(5);
        //modifier un User
        //userService.updateUser(6,"Antoine");
        //verifier l'utilidateur
        String mail = "thoma@gmail.com";
        String plainpassword = "gngngng";
        User user = userService.getUserByMail(mail);
        if (user == null) {
            //Message d'erreur
            System.out.println("Le mot de passse ou le mail est incorrect");
        } else {
            Boolean result = user.checkpassword(plainpassword, user.getPassword());
            if (result == true) {
                //affichage page Dashboard
                System.out.println("Autentification reussi");
            } else {
                //Message d'erreur
                System.out.println("Le mot de passse ou le mail est incorrect");
            }
        }

        launch(args);
    }
}
