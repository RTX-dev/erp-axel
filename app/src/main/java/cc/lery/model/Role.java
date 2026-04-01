package cc.lery.model;

public class Role {
    private Long idRole;
    private String nameFunction;
    private String description;

    public Role() {
    }

    public Role(Long idRole, String nameFunction, String description) {
        this.idRole = idRole;
        this.nameFunction = nameFunction;
        this.description = description;
    }

    // Getters et Setters
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
