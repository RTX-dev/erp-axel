package cc.lery.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cc.lery.dao.SampleDAO;
import cc.lery.model.Sample;

/**
 * SampleService : Gère la logique métier des prélèvements.
 * Fait le pont entre les contrôleurs (UI) et le SampleDAO (base de données).
 */
public class SampleService {

    private final SampleDAO sampleDAO;

    public SampleService() {
        this.sampleDAO = new SampleDAO();
    }

    /**
     * Récupère tous les prélèvements.
     */
    public List<Sample> listAllSamples() {
        try {
            return sampleDAO.getAllSamples();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des prélèvements : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère un prélèvement par son ID.
     */
    public Sample getSampleById(Long id) {
        try {
            return sampleDAO.getSampleById(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du prélèvement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Crée un nouveau prélèvement.
     * @return L'ID généré, ou -1 en cas d'erreur.
     */
    public long addSample(String name, String description, LocalDate receptionDate, String namePatient) {
        Sample sample = new Sample(name, description, receptionDate, namePatient);
        try {
            return sampleDAO.createSample(sample);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du prélèvement : " + e.getMessage());
            return -1;
        }
    }

    /**
     * Met à jour un prélèvement existant.
     */
    public void updateSample(Long id, String name, String description, LocalDate receptionDate, String namePatient) {
        try {
            Sample sample = sampleDAO.getSampleById(id);
            if (sample != null) {
                sample.setName(name);
                sample.setDescription(description);
                sample.setReceptionDate(receptionDate);
                sample.setNamePatient(namePatient);
                sampleDAO.updateSample(sample);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du prélèvement : " + e.getMessage());
        }
    }

    /**
     * Supprime un prélèvement.
     */
    public void deleteSample(Long id) {
        try {
            sampleDAO.deleteSample(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du prélèvement : " + e.getMessage());
        }
    }
}
