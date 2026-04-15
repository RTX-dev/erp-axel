package cc.lery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cc.lery.model.Sample;

/**
 * SampleDAO : Gère toutes les opérations SQL sur la table 'sample'.
 */
public class SampleDAO {

    private static final String URL = "jdbc:mariadb://127.0.0.1:3307/erp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Récupère tous les prélèvements.
     */
    public List<Sample> getAllSamples() throws SQLException {
        List<Sample> list = new ArrayList<>();
        String sql = "SELECT * FROM sample";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql); //stmt préparer la rquêtee SQL + rs = on execute et on récupère les résultats + conn = ouvre la connextion a la DB
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { //  On parcourt chaque ligne du résultat une par une
                list.add(new Sample(
                    rs.getLong("id_sample"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("date_of_create").toLocalDate(),
                    rs.getDate("reception_date").toLocalDate(),
                    rs.getString("name_patient")
                ));
            }
        }
        return list;
    }

    /**
     * Récupère un prélèvement par son ID.
     */
    public Sample getSampleById(Long id) throws SQLException {
        String sql = "SELECT * FROM sample WHERE id_sample = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Sample(
                    rs.getLong("id_sample"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("date_of_create").toLocalDate(),
                    rs.getDate("reception_date").toLocalDate(),
                    rs.getString("name_patient")
                );
            }
        }
        return null;
    }

    /**
     * Crée un nouveau prélèvement en base.
     * @return L'ID généré automatiquement.
     */
    public long createSample(Sample sample) throws SQLException {
        // L'id_sample est généré automatiquement par la DB grâce à AUTO_INCREMENT
        String sql = "INSERT INTO sample (name, description, date_of_create, reception_date, name_patient) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sample.getName());
            stmt.setString(2, sample.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(sample.getDateOfCreate()));
            stmt.setDate(4, java.sql.Date.valueOf(sample.getReceptionDate()));
            stmt.setString(5, sample.getNamePatient());
            stmt.executeUpdate();

            // Récupère l'ID généré automatiquement par la DB
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1;
    }

    /**
     * Met à jour un prélèvement existant.
     */
    public void updateSample(Sample sample) throws SQLException {
        String sql = "UPDATE sample SET name = ?, description = ?, reception_date = ?, name_patient = ? WHERE id_sample = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sample.getName());
            stmt.setString(2, sample.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(sample.getReceptionDate()));
            stmt.setString(4, sample.getNamePatient());
            stmt.setLong(5, sample.getIdSample());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime un prélèvement par son ID.
     */
    public void deleteSample(Long id) throws SQLException {
        String sql = "DELETE FROM sample WHERE id_sample = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
//stmt préparer la rquêtee SQL + rs = on execute et on récupère les résultats + conn = ouvre la connextion a la DB