package cc.lery.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.dao.RoleDAO;
import cc.lery.model.Role;

/**
 * RoleService : Classe de "logique métier" pour les rôles.
 * Elle sert de pont entre le contrôleur et le DAO (Data Access Object).
 */
public class RoleService {

    // Instance de RoleDAO pour effectuer les requêtes SQL réelles.
    private final RoleDAO roleDAO;

    /**
     * Initialise le DAO lors de la création du service.
     */
    public RoleService() {
        this.roleDAO = new RoleDAO();
    }

    /**
     * AJOUT D'UN RÔLE EN BASE DE DONNÉES.
     * Cette méthode est appelée par le contrôleur.
     */
    public void addRole(String nameFunction, String description) {
        Role newRole = new Role(null, nameFunction, description);
        try {
            // Appelle le DAO pour enregistrer le rôle physiquement en SQL.
            roleDAO.createRole(newRole);
            System.out.println("Rôle '" + nameFunction + "' ajouté avec succès !");
        } catch (SQLException e) {
            // Si SQL échoue (ex: rôle déjà existant), affiche l'erreur dans la console.
            System.err.println("Erreur lors de l'ajout du rôle : " + e.getMessage());
        }
    }

    /**
     * RÉCUPÉRATION DE TOUS LES RÔLES.
     * @return La liste complète des rôles pour remplir la liste déroulante (ComboBox).
     */
    public List<Role> getAllRoles() {
        try {
            // Appelle le DAO pour faire un SELECT * FROM role.
            return roleDAO.getAllRoles();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rôles : " + e.getMessage());
            return new ArrayList<>(); // Retourne une liste vide en cas d'échec SQL.
        }
    }

    /**
     * RÉCUPÉRATION D'UN RÔLE PRÉCIS PAR SON ID.
     */
    public Role getRoleById(Long id) {
        try {
            return roleDAO.getRoleById(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du rôle : " + e.getMessage());
            return null;
        }
    }

    /**
     * RÉCUPÉRATION DU NOM DU RÔLE D'UN UTILISATEUR.
     * @param userId ID de l'utilisateur concerné.
     * @return Le nom (ex: ADMIN, MEDECIN) stocké dans la table de jointure Assign.
     */
    public String getUserRoleName(int userId) {
        try {
            // Fait la jointure en base de données pour trouver le rôle lié à l'ID de l'utilisateur.
            return roleDAO.getUserRoleName(userId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du rôle de l'utilisateur : " + e.getMessage());
            return null;
        }
    }

    /**
     * ASSIGNATION D'UN RÔLE À UN UTILISATEUR.
     * Appelé juste après la création d'un utilisateur dans la table 'users'.
     * @param userId ID de l'utilisateur créé.
     * @param roleId ID du rôle à lui donner (1 ou 2).
     */
    public void assignRoleToUser(int userId, Long roleId) {
        try {
            // Crée le lien dans la table Assign : id_user -> id_role.
            roleDAO.assignRoleToUser(userId, roleId);
            System.out.println("Rôle " + roleId + " assigné à l'utilisateur " + userId + " avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation du rôle à l'utilisateur : " + e.getMessage());
        }
    }
}
