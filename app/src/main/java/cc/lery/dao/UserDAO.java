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
        String sql = "SELECT * FROM " + tablename + " WHERE id = ? ";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), 
                                        rs.getString("lastname"),
                                        rs.getString("firstname"),
                                        rs.getString("mail"),
                                        rs.getString("phoneNumber"));
                return user;
            }
        }
        return null;
    }

    public User getUserByMail(String mail) throws SQLException {
        String sql = "SELECT * FROM " + tablename + " WHERE mail = ? ";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), 
                                        rs.getString("lastname"),
                                        rs.getString("firstname"),
                                        rs.getString("mail"),
                                        rs.getString("phoneNumber"),
                                        rs.getString("password"));
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM "+ tablename;
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), 
                                        rs.getString("lastname"),
                                        rs.getString("firstname"),
                                        rs.getString("mail"),
                                        rs.getString("phoneNumber"));
                userList.add(user);
            }
        }
        return userList;
    }

    // Méthode pour créer un nouvel utilisateur
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO " + tablename + " (lastname, firstname, mail, phoneNumber, password) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection(); // PrepareStatement fait une requête préparée
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            String hashedpassword = user.hashpassword(user.getPassword());
            stmt.setString(1, user.getLastname());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getMail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, hashedpassword);
            stmt.executeUpdate();
        }
    }

    public void updateUser(int id, String lastname, String firstname, String mail, String phoneNumber, String password) throws SQLException {
        String sql = "UPDATE "+ tablename +" SET lastname = ?, firstname = ?, mail = ?, phoneNumber = ?, password = ? WHERE id = ?";
        try (Connection conn = getConnection(); // PrepareStatement fait une requête préparée
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lastname);
            stmt.setString(2, firstname);
            stmt.setString(3, mail);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, password);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(int id) throws SQLException{
        String sql = "DELETE FROM "+ tablename +" WHERE id = ?";
        try (Connection conn = getConnection(); // PrepareStatement fait une requête préparée
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,id);
            stmt.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès !");
        } 
    }
}
