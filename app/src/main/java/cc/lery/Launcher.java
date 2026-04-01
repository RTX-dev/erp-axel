package cc.lery;

import cc.lery.model.User;
import cc.lery.service.RoleService;
import cc.lery.service.UserService;
import javafx.application.Application;

/**
 * Le Launcher est la classe principale (point d'entrée) du programme.
 * Elle est responsable de l'initialisation de l'application (base de données, comptes par défaut, etc.)
 * avant le lancement de l'interface graphique JavaFX.
 */
public class Launcher {

    /**
     * Méthode main du programme exécutée en premier.
     * C'est cette méthode que Gradle appelle lorsqu'on exécute la commande './gradlew run'.
     */
    public static void main(String[] args) {
        
        // Initialisation des services pour interagir avec la base de données.
        UserService userService = new UserService();
        RoleService roleService = new RoleService();

        try {
            // --- GÉNÉRATION AUTOMATIQUE DES COMPTES DE TEST (S'ILS N'EXISTENT PAS DÉJÀ) ---
            // Cette section permet de s'assurer que vous aurez toujours un compte admin pour tester.
            System.out.println("Vérification des comptes de test...");
            
            // 1. Création de l'administrateur par défaut (ID_ROLE 1 = ADMIN)
            if (userService.getUserByMail("admin@erp.com") == null) {
                System.out.println("Compte ADMIN manquant. Création...");
                int adminId = userService.addUser("Admin", "Axel", "admin@erp.com", "0600000001", "admin123");
                if (adminId != -1) {
                    // Si l'utilisateur est bien créé, on l'assigne au rôle ADMIN dans la table de jointure Assign.
                    roleService.assignRoleToUser(adminId, 1L); 
                    System.out.println("Compte ADMIN créé avec succès (ID: " + adminId + ")");
                }
            } else {
                System.out.println("Compte ADMIN déjà présent.");
            }

            // 2. Création du médecin par défaut (ID_ROLE 2 = MEDECIN)
            if (userService.getUserByMail("doc@erp.com") == null) {
                System.out.println("Compte MEDECIN manquant. Création...");
                int docId = userService.addUser("Medecin", "Thomas", "doc@erp.com", "0600000002", "doc123");
                if (docId != -1) {
                    // Si l'utilisateur est bien créé, on l'assigne au rôle MEDECIN dans la table de jointure Assign.
                    roleService.assignRoleToUser(docId, 2L);
                    System.out.println("Compte MEDECIN créé avec succès (ID: " + docId + ")");
                }
            } else {
                System.out.println("Compte MEDECIN déjà présent.");
            }
        } catch (Exception e) {
            // Affiche l'erreur si la connexion DB ou l'insertion échoue.
            System.err.println("Erreur lors de l'initialisation des données : " + e.getMessage());
        }

        /**
         * Lance l'application JavaFX. 
         * Cela va créer une instance de AppScene et appeler sa méthode start().
         * C'est ici que l'interface graphique démarre réellement.
         */
        Application.launch(AppScene.class, args);
    }
}
