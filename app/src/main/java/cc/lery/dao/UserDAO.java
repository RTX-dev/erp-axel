package cc.lery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.model.User;

public class UserDAO {

    private static final String URL = "jdbc:mariadb://localhost:3306/erp";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    String tablename = "users";
    
    // Méthode pour obtenir une connexion à la base de données
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT lastname, id FROM " + tablename + " WHERE id = ? ";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("lastname"));
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id, lastname FROM users";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("lastname"));
                userList.add(user);
            }
        }
        return userList;
    }

    // Méthode pour créer un nouvel utilisateur
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (lastname) VALUES (?)";
        try (Connection conn = getConnection(); // PrepareStatement fait une requête préparée
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getLastname());
            stmt.executeUpdate();
        }
    }

    public void updateUser(int id, String newName) throws SQLException {
        String sql = "UPDATE users SET lastname = ? WHERE id = ?";
        try (Connection conn = getConnection(); // PrepareStatement fait une requête préparée
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(int id) throws SQLException{
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection(); // PrepareStatement fait une requête préparée
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,id);
            stmt.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès !");
        } 
    }
}
