package cc.lery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Import nécessaire pour Statement.RETURN_GENERATED_KEYS
import java.util.ArrayList;
import java.util.List;

import cc.lery.model.User;

public class UserDAO {

    // URL de connexion à la base de données MariaDB.
    // Utilise 127.0.0.1 (IPv4 localhost) et le port 3307 (spécifique à XAMPP sur Mac).
    // La base de données ciblée est 'erp'.
    private static final String URL = "jdbc:mariadb://127.0.0.1:3307/erp";
    private static final String USER = "root"; // Nom d'utilisateur de la base de données
    private static final String PASSWORD = ""; // Mot de passe de la base de données (vide dans ce cas)
    
    // Le nom de la table des utilisateurs dans la base de données.
    // Correspond à la table 'users' de votre schéma SQL.
    private final String tablename = "users";
    
    /**
     * Établit et retourne une connexion à la base de données.
     * @return Une instance de Connection.
     * @throws SQLException Si une erreur de connexion survient.
     */
    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Affiche l'erreur de connexion dans la console pour faciliter le débogage.
            System.err.println("ERREUR CONNECTION (Port 3307) : " + e.getMessage());
            throw e; // Propage l'exception pour être gérée plus haut dans la pile d'appels.
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     * @param id L'ID de l'utilisateur.
     * @return L'objet User correspondant, ou null si non trouvé.
     * @throws SQLException Si une erreur SQL survient.
     */
    public User getUserById(int id) throws SQLException {
        // Requête SQL pour sélectionner un utilisateur par son ID.
        // Utilise 'users' comme nom de table et 'id_user' comme colonne ID.
        String sql = "SELECT * FROM users WHERE id_user = ? ";
        try (Connection conn = getConnection(); // Obtient une connexion.
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prépare la requête.
            stmt.setInt(1, id); // Définit le paramètre de l'ID.
            ResultSet rs = stmt.executeQuery(); // Exécute la requête.
            if (rs.next()) { // Si un résultat est trouvé.
                // Crée et retourne un objet User avec les données de la base.
                // L'ordre des paramètres correspond au constructeur de User: id, lastname, firstname, mail, phoneNumber, password.
                return new User(rs.getInt("id_user"), 
                                rs.getString("lastname"), // Colonne 'lastname' de la DB
                                rs.getString("firstname"), // Colonne 'firstname' de la DB
                                rs.getString("mail"), // Colonne 'mail' de la DB
                                rs.getString("phone")); // Colonne 'phone' de la DB
            }
        }
        return null; // Retourne null si aucun utilisateur n'est trouvé.
    }

    /**
     * Récupère un utilisateur par son adresse e-mail.
     * @param mail L'adresse e-mail de l'utilisateur.
     * @return L'objet User correspondant, ou null si non trouvé.
     * @throws SQLException Si une erreur SQL survient.
     */
    public User getUserByMail(String mail) throws SQLException {
        // Requête SQL pour sélectionner un utilisateur par son e-mail.
        String sql = "SELECT * FROM users WHERE mail = ? ";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mail); // Définit le paramètre de l'e-mail.
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Crée et retourne un objet User avec les données de la base, y compris le mot de passe.
                return new User(rs.getInt("id_user"), 
                                rs.getString("lastname"),
                                rs.getString("firstname"),
                                rs.getString("mail"),
                                rs.getString("phone"),
                                rs.getString("password")); // Colonne 'password' de la DB
            }
        }
        return null;
    }

    /**
     * Récupère tous les utilisateurs de la base de données.
     * @return Une liste d'objets User.
     * @throws SQLException Si une erreur SQL survient.
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users"; // Requête SQL pour sélectionner tous les utilisateurs.
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // Parcourt tous les résultats.
                User user = new User(rs.getInt("id_user"), 
                                     rs.getString("lastname"),
                                     rs.getString("firstname"),
                                     rs.getString("mail"),
                                     rs.getString("phone"));
                userList.add(user); // Ajoute l'utilisateur à la liste.
            }
        }
        return userList;
    }

    /**
     * Crée un nouvel utilisateur dans la base de données.
     * @param user L'objet User à créer.
     * @return L'ID généré pour le nouvel utilisateur, ou -1 si l'insertion échoue.
     * @throws SQLException Si une erreur SQL survient.
     */
    public int createUser(User user) throws SQLException {
        // Requête SQL pour insérer un nouvel utilisateur.
        // Les colonnes 'lastname', 'firstname', 'mail', 'phone', 'password' sont spécifiées.
        String sql = "INSERT INTO users (lastname, firstname, mail, phone, password) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection(); 
             // Prépare la requête et demande de retourner les clés générées (pour l'AUTO_INCREMENT).
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String hashedpassword = user.hashpassword(user.getPassword()); // Hache le mot de passe avant insertion.
            stmt.setString(1, user.getLastname());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getMail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, hashedpassword);
            stmt.executeUpdate(); // Exécute la requête d'insertion.
            
            // Récupère l'ID généré par la base de données (AUTO_INCREMENT).
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Retourne le premier ID généré.
            }
        }
        return -1; // Retourne -1 si aucun ID n'a été généré.
    }

    /**
     * Met à jour les informations d'un utilisateur existant.
     * @param id L'ID de l'utilisateur à mettre à jour.
     * @param lastname Le nouveau nom de famille.
     * @param firstname Le nouveau prénom.
     * @param mail La nouvelle adresse e-mail.
     * @param phone Le nouveau numéro de téléphone.
     * @param password Le nouveau mot de passe (haché).
     * @throws SQLException Si une erreur SQL survient.
     */
    public void updateUser(int id, String lastname, String firstname, String mail, String phone, String password) throws SQLException {
        // Requête SQL pour mettre à jour un utilisateur par son ID.
        String sql = "UPDATE users SET lastname = ?, firstname = ?, mail = ?, phone = ?, password = ? WHERE id_user = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lastname);
            stmt.setString(2, firstname);
            stmt.setString(3, mail);
            stmt.setString(4, phone);
            stmt.setString(5, password);
            stmt.setInt(6, id); // L'ID de l'utilisateur à mettre à jour.
            stmt.executeUpdate(); // Exécute la requête de mise à jour.
        }
    }

    /**
     * Supprime un utilisateur de la base de données.
     * @param id L'ID de l'utilisateur à supprimer.
     * @throws SQLException Si une erreur SQL survient.
     */
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id_user = ?"; // Requête SQL pour supprimer un utilisateur par son ID.
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // L'ID de l'utilisateur à supprimer.
            stmt.executeUpdate(); // Exécute la requête de suppression.
        }
    }
}
