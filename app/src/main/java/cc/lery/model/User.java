package cc.lery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lastname")// Inutile ici car le nom de la colonne (lastname) est identique au nom de la variable (lastname)
    private String lastname;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec paramètres
    public User(int id ,String nom) {
        this.id = id;
        this.lastname = nom;
    }

    // Getters et Setters
    // On doit définir les getters et setters de tous les champs 
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String name) {
        this.lastname = name;
    }
}
