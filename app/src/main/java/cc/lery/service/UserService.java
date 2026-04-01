package cc.lery.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.dao.UserDAO;
import cc.lery.model.User;
import cc.lery.session.SessionManager;

/**
 * UserService : Classe de "logique métier" pour les utilisateurs.
 * C'est le cerveau qui gère l'ajout, la suppression et surtout la validation (login) des utilisateurs.
 */
public class UserService {

    // Instance de UserDAO pour effectuer les requêtes SQL réelles.
    private final UserDAO userDAO;
    // Instance de RoleService pour gérer les rôles liés aux utilisateurs.
    private final RoleService roleService;

    /**
     * Initialise les DAOs et Services nécessaires lors de la création du service.
     */
    public UserService() {
        this.userDAO = new UserDAO();
        this.roleService = new RoleService();
    }

    /**
     * AJOUT D'UN NOUVEL UTILISATEUR.
     * Cette méthode est appelée par le contrôleur 'AddUserController'.
     * @return L'ID généré par la base de données (pour pouvoir ensuite assigner un rôle).
     */
    public int addUser(String lastname, String firstname, String mail, String phonenumber, String password) {
        // Crée un objet User avec les données du formulaire. L'ID est à 0 (sera ignoré par l'INSERT).
        User newUser = new User(0, lastname, firstname, mail, phonenumber, password);
        try {
            // Appelle le DAO pour enregistrer l'utilisateur physiquement en SQL.
            int generatedId = userDAO.createUser(newUser);
            System.out.println("Utilisateur ajouté avec l'ID : " + generatedId);
            return generatedId; // Retourne l'ID généré par l'AUTO_INCREMENT.
        } catch (SQLException e) {
            // Si SQL échoue (ex: e-mail déjà présent), affiche l'erreur dans la console.
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return -1; // Retourne -1 en cas d'échec SQL.
        }
    }

    /**
     * RÉCUPÉRATION D'UN UTILISATEUR PAR SON ID.
     * @return L'utilisateur avec son rôle ("ADMIN" ou "MEDECIN") attaché.
     */
    public User getUserById(int id) {
        try {
            // 1. Récupère d'abord les infos de base (nom, prénom...) dans la table users.
            User user = userDAO.getUserById(id);
            if (user != null) {
                // 2. Si l'utilisateur est trouvé, récupère son nom de rôle via la table de jointure Assign.
                String roleName = roleService.getUserRoleName(id);
                // Si l'utilisateur n'a aucun rôle, on lui donne le rôle "USER" par défaut.
                user.setRole(roleName != null ? roleName : "USER");
            }
            return user;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return null;
        }
    }

    /**
     * RÉCUPÉRATION D'UN UTILISATEUR PAR SON E-MAIL.
     * Utilisé principalement pour la connexion (LOGIN).
     */
    public User getUserByMail(String mail) {
        try {
            // 1. Récupère l'utilisateur par son e-mail dans la table users.
            User user = userDAO.getUserByMail(mail);
            if (user != null) {
                // 2. On attache son rôle (nécessaire pour limiter les accès au Dashboard).
                String roleName = roleService.getUserRoleName(user.getId());
                user.setRole(roleName != null ? roleName : "USER");
            }
            return user;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return null;
        }
    }

    /**
     * RÉCUPÉRATION DE TOUS LES UTILISATEURS.
     * @return La liste complète des utilisateurs avec leurs rôles attachés.
     */
    public List<User> listAllUsers() {
        try {
            // 1. Récupère la liste complète des utilisateurs dans la table users.
            List<User> users = userDAO.getAllUsers();
            // 2. Pour chaque utilisateur, attache son nom de rôle (ADMIN, MEDECIN...).
            for (User user : users) {
                String roleName = roleService.getUserRoleName(user.getId());
                user.setRole(roleName != null ? roleName : "USER");
            }
            return users;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * METTRE À JOUR UN UTILISATEUR.
     * Cette méthode hache d'abord le mot de passe avant d'envoyer la donnée au DAO.
     */
    public void updateUser(int id, String newLastName, String newFirstName , String mail , String Tel , String password) {
        try {
            User user = userDAO.getUserById(id);
            if (user != null) {
                // Hacher le nouveau mot de passe (si modifié) avant de le sauvegarder en SQL.
                String hashedPassword = user.hashpassword(password);
                
                // Appelle la méthode DAO pour mettre à jour la base de données.
                userDAO.updateUser(id, newLastName, newFirstName, mail, Tel, hashedPassword);
                
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * SUPPRIMER UN UTILISATEUR.
     */
    public void deleteUser(int id) {
        try {
            // Appelle le DAO pour faire un DELETE FROM users WHERE id_user = id.
            userDAO.deleteUser(id);
            System.out.println("Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * VALIDATION DE LA CONNEXION (LOGIN).
     * @param email L'adresse e-mail saisie par l'utilisateur.
     * @param password Le mot de passe saisi par l'utilisateur.
     * @return true si la connexion est réussie, false sinon.
     */
    public boolean validateUser(String email, String password) {
        try {
            // 1. Récupère l'utilisateur par son mail (inclut son rôle ADMIN/MEDECIN).
            User user = getUserByMail(email); 
            if (user == null) {
                System.out.println("Email non trouvé en base de données.");
                return false;
            } else if (user.checkpassword(password, user.getPassword())) {
                // 2. Vérifie si le mot de passe correspond au hachage BCrypt en DB.
                // 3. SI OUI: On connecte l'utilisateur dans la session globale (SessionManager).
                SessionManager.login(user);
                System.out.println("Connexion réussie ! Rôle : " + user.getRole());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
