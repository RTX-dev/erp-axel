package cc.lery.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.lery.dao.RoleDAO;
import cc.lery.model.Role;

/**
 * RoleService : Gère la logique métier des rôles.
 */
public class RoleService {

    private final RoleDAO roleDAO;

    public RoleService() {
        this.roleDAO = new RoleDAO();
    }

    /**
     * CRÉER UN NOUVEAU RÔLE.
     */
    public void addRole(String nameFunction, String description) {
        Role newRole = new Role(null, nameFunction, description);
        try {
            roleDAO.createRole(newRole);
        } catch (SQLException e) {
            System.err.println("Erreur ajout rôle : " + e.getMessage());
        }
    }

    /**
     * RÉCUPÉRER TOUS LES RÔLES DISPONIBLES (pour les afficher dans une liste).
     */
    public List<Role> getAllRoles() {
        try {
            return roleDAO.getAllRoles();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * ASSIGNER UN RÔLE À UN UTILISATEUR.
     * Crée le lien dans la table 'Assign'.
     */
    public void assignRoleToUser(int userId, Long roleId) {
        try {
            roleDAO.assignRoleToUser(userId, roleId);
        } catch (SQLException e) {
            System.err.println("Erreur assignation rôle : " + e.getMessage());
        }
    }
}
