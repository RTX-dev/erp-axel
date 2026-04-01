package cc.lery.model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Modèle de données représentant un Utilisateur.
 * Contient les informations personnelles, le mot de passe haché et le rôle de l'utilisateur.
 */
public class User {

    private Integer id; // ID de l'utilisateur (généré par la base de données)
    private String lastname; // Nom de famille
    private String firstname; // Prénom
    private String mail; // Adresse e-mail (unique en DB)
    private String phoneNumber; // Numéro de téléphone
    private String password; // Mot de passe (stocké sous forme hachée avec BCrypt)
    private String role; // Rôle de l'utilisateur (ex: "ADMIN", "MEDECIN")

    // Constructeur par défaut nécessaire pour certains frameworks ou manipulations simples.
    public User() {
    }

    /**
     * Constructeur complet pour initialiser un utilisateur avec toutes ses informations.
     * Utilisé lors de la récupération des données depuis la base de données.
     */
    public User(int id ,String nom, String prenom,String mail, String telephone, String mdp) {
        this.id = id;
        this.lastname = nom;
        this.firstname = prenom;
        this.mail = mail;
        this.phoneNumber = telephone;
        this.password = mdp;
    }

    /**
     * Constructeur simplifié sans mot de passe.
     * Utilisé pour l'affichage dans les listes (TableView) où le mot de passe n'est pas nécessaire.
     */
    public User(int id ,String nom, String prenom,String mail, String telephone) {
        this(id, nom, prenom, mail, telephone, null);
    }

    // Getters et Setters pour accéder et modifier les propriétés de l'objet de manière sécurisée.
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLastname() { return lastname; }
    public void setLastname(String name) { this.lastname = name; }
    
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    /**
     * Hache un mot de passe en utilisant l'algorithme BCrypt.
     * Cette méthode est utilisée avant d'enregistrer le mot de passe dans la base de données.
     * @param password Le mot de passe en clair.
     * @return Le mot de passe haché et sécurisé.
     */
    public String hashpassword(String password){
        // BCrypt génère un "salt" aléatoire pour renforcer la sécurité du hachage.
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return hashed;
    }

    /**
     * Vérifie si un mot de passe en clair correspond au mot de passe haché stocké.
     * Utilisé lors de la connexion de l'utilisateur.
     * @param plainPassword Le mot de passe saisi par l'utilisateur.
     * @param hashPassword Le mot de passe haché récupéré de la base de données.
     * @return true si les mots de passe correspondent, false sinon.
     */
    public Boolean checkpassword (String plainPassword,String hashPassword){
        // BCrypt compare les deux valeurs de manière sécurisée.
        return BCrypt.checkpw(plainPassword, hashPassword);
    }
}
