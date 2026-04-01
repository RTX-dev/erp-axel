package cc.lery;

import cc.lery.model.User;
import cc.lery.service.RoleService;
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
        
        UserService userService = new UserService();
        RoleService roleService = new RoleService();

        try {
            // --- GÉNÉRATION DES COMPTES DE TEST ---
            
            // 1. Création de l'administrateur
            if (userService.getUserByMail("admin@erp.com") == null) {
                System.out.println("Compte ADMIN manquant. Création...");
                int adminId = userService.addUser("Admin", "Axel", "admin@erp.com", "0600000001", "admin123");
                if (adminId != -1) {
                    roleService.assignRoleToUser(adminId, 1L); // 1 = ADMIN
                    System.out.println("Compte ADMIN créé avec succès (ID: " + adminId + ")");
                }
            } else {
                System.out.println("Compte ADMIN déjà présent.");
            }

            // 2. Création du médecin
            if (userService.getUserByMail("doc@erp.com") == null) {
                System.out.println("Compte MEDECIN manquant. Création...");
                int docId = userService.addUser("Medecin", "Thomas", "doc@erp.com", "0600000002", "doc123");
                if (docId != -1) {
                    roleService.assignRoleToUser(docId, 2L); // 2 = MEDECIN
                    System.out.println("Compte MEDECIN créé avec succès (ID: " + docId + ")");
                }
            } else {
                System.out.println("Compte MEDECIN déjà présent.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation des données : " + e.getMessage());
        }

        launch(args);
    }
}
