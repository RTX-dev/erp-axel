package cc.lery.model;

import java.time.LocalDate;

/**
 * Modèle Sample : Représente un prélèvement dans l'application.
 * Correspond à la table 'sample' en base de données.
 */
public class Sample {

    private Long idSample;
    private String name;
    private String description;
    private LocalDate dateOfCreate;
    private LocalDate receptionDate;
    private String namePatient;

    public Sample() {}
    public Sample(Long idSample, String name, String description, LocalDate dateOfCreate, LocalDate receptionDate, String namePatient) {
        this.idSample = idSample;
        this.name = name;
        this.description = description;
        this.dateOfCreate = dateOfCreate;
        this.receptionDate = receptionDate;
        this.namePatient = namePatient;
    }

    /** Constructeur sans ID (pour la création, l'ID est généré par la DB) */
    public Sample(String name, String description, LocalDate receptionDate, String namePatient) {
        this.name = name;
        this.description = description;
        this.dateOfCreate = LocalDate.now();
        this.receptionDate = receptionDate;
        this.namePatient = namePatient;
    }

    // --- GETTERS ET SETTERS ---

    public Long getIdSample() { return idSample; }
    public void setIdSample(Long idSample) { this.idSample = idSample; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateOfCreate() { return dateOfCreate; }
    public void setDateOfCreate(LocalDate dateOfCreate) { this.dateOfCreate = dateOfCreate; }

    public LocalDate getReceptionDate() { return receptionDate; }
    public void setReceptionDate(LocalDate receptionDate) { this.receptionDate = receptionDate; }

    public String getNamePatient() { return namePatient; }
    public void setNamePatient(String namePatient) { this.namePatient = namePatient; }
}