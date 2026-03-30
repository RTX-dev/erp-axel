package cc.lery.model;

import org.mindrot.jbcrypt.BCrypt;

public class User {

    private Integer id;
    private String lastname;
    private String firstname;
    private String mail;
    private String phoneNumber;
    private String password;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec paramètres
    public User(int id ,String nom, String prenom,String mail, String telephone, String mdp) {
        this.id = id;
        this.lastname = nom;
        this.firstname = prenom;
        this.mail = mail;
        this.phoneNumber = telephone;
        this.password = mdp;
    }

    public User(int id ,String nom, String prenom,String mail, String telephone) {
        this(id, nom, prenom, mail, telephone,null);
    }

    
    // Getters et Setters
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String hashpassword(String password){
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return hashed;
    }

    public Boolean checkpassword (String plainPassword,String hashPassword){
        /*if (BCrypt.checkpw(plainPassword, hashPassword))
	        System.out.println("It matches");
        else
	        System.out.println("It does not match");*/
        return BCrypt.checkpw(plainPassword, hashPassword);
    }
}
