package cc.lery.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.dao.UserDAO;
import cc.lery.dao.RoleDAO;
import cc.lery.model.User;
import cc.lery.session.SessionManager;

/**
 * UserService : Gère la logique métier des utilisateurs.
 * Elle fait le pont entre l'interface (Contrôleurs) et la base de données (DAOs).
 */
public class UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
    }

    /**
     * AJOUTER UN UTILISATEUR.
     * @return L'ID de l'utilisateur créé, ou -1 en cas d'erreur.
     */
    public int addUser(String lastname, String firstname, String mail, String phonenumber, String password) {
        User newUser = new User(0, lastname, firstname, mail, phonenumber, password);
        try {
            return userDAO.createUser(newUser);
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout : " + e.getMessage());
            return -1;
        }
    }

    /**
     * RÉCUPÉRER UN UTILISATEUR PAR SON ID (avec tous ses rôles).
     */
    public User getUserById(int id) {
        try {
            User user = userDAO.getUserById(id);
            if (user != null) {
                // On récupère la LISTE des rôles depuis la table de jointure
                List<String> roles = roleDAO.getUserRoles(id);
                user.setRoles(roles);
            }
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * RÉCUPÉRER UN UTILISATEUR PAR SON E-MAIL (avec tous ses rôles).
     */
    public User getUserByMail(String mail) {
        try {
            User user = userDAO.getUserByMail(mail);
            if (user != null) {
                List<String> roles = roleDAO.getUserRoles(user.getId());
                user.setRoles(roles);
            }
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * LISTER TOUS LES UTILISATEURS DU SYSTÈME.
     */
    public List<User> listAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                // Pour chaque utilisateur, on va chercher ses rôles en base
                user.setRoles(roleDAO.getUserRoles(user.getId()));
            }
            return users;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * METTRE À JOUR LES INFOS D'UN UTILISATEUR.
     */
    public void updateUser(int id, String newLastName, String newFirstName , String mail , String Tel , String password) {
        try {
            User user = userDAO.getUserById(id);
            if (user != null) {
                String hashedPassword = user.hashpassword(password);
                userDAO.updateUser(id, newLastName, newFirstName, mail, Tel, hashedPassword);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    /**
     * SUPPRIMER DÉFINITIVEMENT UN UTILISATEUR.
     */
    public void deleteUser(int id) {
        try {
            userDAO.deleteUser(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    /**
     * VÉRIFIER LA CONNEXION (Login).
     * @return true si l'email existe et que le mot de passe est correct.
     */
    public boolean validateUser(String email, String password) {
        try {
            User user = getUserByMail(email); 
            if (user != null && user.checkpassword(password, user.getPassword())) {
                // On enregistre l'utilisateur dans la session globale
                SessionManager.login(user);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
        }
        return false;
    }
}
