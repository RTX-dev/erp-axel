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
 * RoleDAO (Data Access Object) : La seule classe qui interagit avec les tables 'role' et 'Assign'.
 */
public class RoleDAO {

    // Identifiants de connexion à la base de données (XAMPP Port 3307).
    private static final String URL = "jdbc:mariadb://127.0.0.1:3307/erp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Crée une connexion JDBC vers MariaDB.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Insère un nouveau rôle dans la table 'role'.
     */
    public void createRole(Role role) throws SQLException {
        String sql = "INSERT INTO role (name_function, description) VALUES (?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getNameFunction()); // Remplacer le 1er ? par le nom (ex: ADMIN)
            stmt.setString(2, role.getDescription()); // Remplacer le 2eme ? par la description
            stmt.executeUpdate(); // Lancer l'insertion
        }
    }

    /**
     * Récupère la liste de tous les rôles existants.
     */
    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM role";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Pour chaque ligne en base, on crée un objet Role en Java.
                roles.add(new Role(rs.getLong("id_role"), rs.getString("name_function"), rs.getString("description")));
            }
        }
        return roles;
    }

    /**
     * Récupère un rôle précis par son ID.
     */
    public Role getRoleById(Long id) throws SQLException {
        String sql = "SELECT * FROM role WHERE id_role = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getLong("id_role"), rs.getString("name_function"), rs.getString("description"));
            }
        }
        return null;
    }

    /**
     * RÉCUPÉRATION DU RÔLE VIA LA TABLE DE JOINTURE 'Assign'.
     * @param userId ID de l'utilisateur.
     * @return Le nom du rôle (ex: "ADMIN", "MEDECIN").
     */
    public String getUserRoleName(int userId) throws SQLException {
        // Cette requête fait une JOINTURE (JOIN) entre 'role' et 'Assign'
        // pour trouver le nom du rôle lié à l'ID de l'utilisateur.
        String sql = "SELECT r.name_function FROM role r " +
                     "JOIN Assign a ON r.id_role = a.id_role " +
                     "WHERE a.id_user = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name_function"); // Retourne "ADMIN" ou "MEDECIN"
            }
        }
        return null; // Retourne null si l'utilisateur n'a aucun rôle assigné
    }

    /**
     * ASSIGNATION D'UN RÔLE DANS LA TABLE DE JOINTURE 'Assign'.
     * @param userId ID de l'utilisateur créé.
     * @param roleId ID du rôle à lui donner (1 pour ADMIN, 2 pour MEDECIN).
     */
    public void assignRoleToUser(int userId, Long roleId) throws SQLException {
        String sql = "INSERT INTO Assign (id_user, id_role) VALUES (?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setLong(2, roleId);
            stmt.executeUpdate(); // Crée le lien dans la base de données
        }
    }
}
