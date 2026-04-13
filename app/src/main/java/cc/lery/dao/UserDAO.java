package cc.lery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cc.lery.model.User;

/**
 * UserDAO (Data Access Object) : Gère toutes les opérations SQL sur la table 'users'.
 * C'est ici qu'on écrit les requêtes SELECT, INSERT, UPDATE et DELETE.
 */
public class UserDAO {

    // Paramètres de connexion à la base de données MariaDB (XAMPP sur Mac utilise souvent le port 3307).
    private static final String URL = "jdbc:mariadb://127.0.0.1:3307/erp";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    /**
     * Crée une connexion à la base de données.
     * On utilise 'try-with-resources' dans les autres méthodes pour fermer la connexion automatiquement.
     */
    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERREUR CONNEXION SQL : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Trouve un utilisateur par son identifiant numérique (ID).
     */
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id_user = ? ";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Remplace le '?' par l'ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // On transforme la ligne SQL en un objet Java 'User'
                return new User(rs.getInt("id_user"), 
                                rs.getString("lastname"), 
                                rs.getString("firstname"), 
                                rs.getString("mail"), 
                                rs.getString("phone"));
            }
        }
        return null;
    }

    /**
     * Trouve un utilisateur par son email (utile pour la connexion/login).
     */
    public User getUserByMail(String mail) throws SQLException {
        String sql = "SELECT * FROM users WHERE mail = ? ";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // On récupère aussi le mot de passe pour pouvoir le vérifier plus tard
                return new User(rs.getInt("id_user"), 
                                rs.getString("lastname"),
                                rs.getString("firstname"),
                                rs.getString("mail"),
                                rs.getString("phone"),
                                rs.getString("password"));
            }
        }
        return null;
    }

    /**
     * Récupère la liste de TOUS les utilisateurs enregistrés.
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id_user"), 
                                     rs.getString("lastname"),
                                     rs.getString("firstname"),
                                     rs.getString("mail"),
                                     rs.getString("phone"));
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * Ajoute un nouvel utilisateur en base de données.
     * @return L'ID généré automatiquement par la base de données (AUTO_INCREMENT).
     */
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (lastname, firstname, mail, phone, password) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection(); 
             // On demande à JDBC de nous renvoyer l'ID généré (Statement.RETURN_GENERATED_KEYS)
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // On hache le mot de passe avant de l'envoyer à la base (sécurité !)
            String hashedpassword = user.hashpassword(user.getPassword());
            
            stmt.setString(1, user.getLastname());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getMail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, hashedpassword);
            
            stmt.executeUpdate(); // Exécute l'insertion
            
            // Récupère l'ID qui vient d'être créé
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    /**
     * Met à jour les informations d'un utilisateur existant.
     */
    public void updateUser(int id, String lastname, String firstname, String mail, String phone, String password) throws SQLException {
        String sql = "UPDATE users SET lastname = ?, firstname = ?, mail = ?, phone = ?, password = ? WHERE id_user = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lastname);
            stmt.setString(2, firstname);
            stmt.setString(3, mail);
            stmt.setString(4, phone);
            stmt.setString(5, password);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime un utilisateur définitivement de la base de données.
     */
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id_user = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
