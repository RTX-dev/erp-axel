package cc.lery.model;

/**
 * Modèle Role : Représente un rôle disponible dans le système (ex: ADMIN, MEDECIN).
 * Correspond à la table 'role' en base de données SQL.
 */
public class Role {
    private Long idRole;         // Identifiant unique du rôle
    private String nameFunction; // Nom du rôle (ex: "ADMIN", "MEDECIN")
    private String description;  // Description de ce que le rôle peut faire

    /** Constructeur vide nécessaire pour la sérialisation. */
    public Role() {
    }

    /** Constructeur pour créer un objet Role complet. */
    public Role(Long idRole, String nameFunction, String description) {
        this.idRole = idRole;
        this.nameFunction = nameFunction;
        this.description = description;
    }

    // --- GETTERS ET SETTERS ---
    
    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getNameFunction() {
        return nameFunction;
    }

    public void setNameFunction(String nameFunction) {
        this.nameFunction = nameFunction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
