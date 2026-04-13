package cc.lery.model;

import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle User : Représente un utilisateur dans l'application.
 * C'est une classe de données (POJO) utilisée pour transporter les informations 
 * entre la base de données et l'interface graphique.
 */
public class User {

    // Propriétés de l'utilisateur correspondant aux colonnes de la table 'users' SQL.
    private Integer id;
    private String lastname;
    private String firstname;
    private String mail;
    private String phoneNumber;
    private String password;
    
    // Liste des rôles de l'utilisateur (ex: ["ADMIN", "MEDECIN"])
    private List<String> roles = new ArrayList<>(); 

    /** Constructeur vide (nécessaire pour certains frameworks comme Hibernate/Spring) */
    public User() {
    }

    /** Constructeur complet pour créer un utilisateur avec toutes ses infos. */
    public User(int id ,String nom, String prenom,String mail, String telephone, String mdp) {
        this.id = id;
        this.lastname = nom;
        this.firstname = prenom;
        this.mail = mail;
        this.phoneNumber = telephone;
        this.password = mdp;
    }

    /** Constructeur sans mot de passe (utilisé pour l'affichage simple). */
    public User(int id ,String nom, String prenom,String mail, String telephone) {
        this(id, nom, prenom, mail, telephone, null);
    }

    // --- GETTERS ET SETTERS ---
    // Ces méthodes permettent d'accéder (get) ou de modifier (set) les propriétés privées.

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

    /** Retourne la liste complète des rôles de l'utilisateur. */
    public List<String> getRoles() {
        return roles;
    }

    /** Remplace toute la liste des rôles par une nouvelle. */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /** Ajoute un seul rôle à la liste existante. */
    public void setRole(String role){
        this.roles.add(role);
    }
    
    /** Vérifie si l'utilisateur possède un rôle spécifique (ex: hasRole("ADMIN")). */
    public boolean hasRole(String roleName) {
        return roles.contains(roleName);
    }

    /** 
     * Hache le mot de passe avant de l'enregistrer en base de données.
     * Utilise BCrypt pour la sécurité (on ne stocke jamais les mots de passe en clair).
     */
    public String hashpassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /** 
     * Vérifie si un mot de passe saisi correspond au hachage stocké en base.
     */
    public Boolean checkpassword (String plainPassword,String hashPassword){
        return BCrypt.checkpw(plainPassword, hashPassword);
    }
}
