package cc.lery.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.dao.UserDAO;
import cc.lery.model.User;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Méthode pour ajouter un nouvel utilisateur
    public void addUser(String lastname, String firstname, String mail, String phonenumber, String password) {
        User newUser = new User(0, lastname, firstname, mail, phonenumber, password); // L'ID sera généré par la base de données
        try {
            userDAO.createUser(newUser);
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // Méthode pour récupérer un utilisateur par son ID
    public User getUserById(int id) {
        try {
            return userDAO.getUserById(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour récupérer un utilisateur par son Mail
    public User getUserByMail(String mail) {
        try {
            return userDAO.getUserByMail(mail);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour lister tous les utilisateurs
    public List<User> listAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Méthode pour mettre à jour un utilisateur
    public void updateUser(int id, String newLastName, String newFirstName , String mail , String Tel , String password) {
        try {
            User user = userDAO.getUserById(id);
            if (user != null) {
                // Hacher le mot de passe avant de le sauvegarder
                String hashedPassword = user.hashpassword(password);
                
                // Appeler la méthode DAO pour sauvegarder en base de données
                userDAO.updateUser(id, newLastName, newFirstName, mail, Tel, hashedPassword);
                
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(int id) {
        try {
            userDAO.deleteUser(id);
            System.out.println("Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    public boolean validateUser(String email, String password) {
        try {
            User user = userDAO.getUserByMail(email);
            if (user == null) {
                System.out.println("email pas bon");
                return false;
            } else if (user.checkpassword(password, user.getPassword())) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }
}
