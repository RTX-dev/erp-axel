package cc.lery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.model.Role;

/**
 * RoleDAO (Data Access Object) : Gère les opérations SQL liées aux rôles.
 * Interagit avec les tables 'role' (définition des rôles) et 'Assign' (lien utilisateur-rôle).
 */
public class RoleDAO {

    // Paramètres de connexion (XAMPP Port 3307).
    private static final String URL = "jdbc:mariadb://127.0.0.1:3307/erp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Établit la connexion avec MariaDB.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Ajoute un nouveau type de rôle dans le système (ex: "TECHNICIEN").
     */
    public void createRole(Role role) throws SQLException {
        String sql = "INSERT INTO role (name_function, description) VALUES (?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getNameFunction());
            stmt.setString(2, role.getDescription());
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère la liste de tous les rôles disponibles en base.
     */
    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM role";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new Role(rs.getLong("id_role"), 
                                   rs.getString("name_function"), 
                                   rs.getString("description")));
            }
        }
        return roles;
    }

    /**
     * Récupère les détails d'un rôle précis par son ID.
     */
    public Role getRoleById(Long id) throws SQLException {
        String sql = "SELECT * FROM role WHERE id_role = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getLong("id_role"), 
                                rs.getString("name_function"), 
                                rs.getString("description"));
            }
        }
        return null;
    }

    /**
     * Récupère TOUS les noms des rôles d'un utilisateur spécifique.
     * @param userId L'identifiant de l'utilisateur.
     * @return Une liste de chaînes (ex: ["ADMIN", "MEDECIN"]).
     */
    public List<String> getUserRoles(int userId) throws SQLException {
        List<String> roles = new ArrayList<>();
        // On fait une jointure entre la table 'role' et la table de liaison 'Assign'.
        String sql = "SELECT r.name_function FROM role r " +
                     "JOIN Assign a ON r.id_role = a.id_role " +
                     "WHERE a.id_user = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("name_function"));
            }
        }
        return roles;
    }

    /**
     * Crée un lien entre un utilisateur et un rôle dans la table de jointure 'Assign'.
     * C'est cette méthode qui permet d'affecter un rôle à quelqu'un.
     */
    public void assignRoleToUser(int userId, Long roleId) throws SQLException {
        String sql = "INSERT INTO Assign (id_user, id_role) VALUES (?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setLong(2, roleId);
            stmt.executeUpdate();
        }
    }
}
