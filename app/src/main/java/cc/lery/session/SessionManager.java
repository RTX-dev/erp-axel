package cc.lery.session;

import cc.lery.model.User;

/**
 * SessionManager : Gère l'utilisateur actuellement connecté.
 * Toutes ses méthodes sont 'static', ce qui signifie qu'on peut les appeler 
 * partout dans l'application sans créer d'objet (ex: SessionManager.isAdmin()).
 */
public class SessionManager {

    // L'utilisateur actuellement connecté au système (null si personne n'est connecté).
    private static User currentUser;

    /**
     * Enregistre l'utilisateur au moment de la connexion réussie.
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Récupère l'objet User complet de la personne connectée.
     */
    public static User getUser() {
        return currentUser;
    }

    /**
     * Déconnecte l'utilisateur en vidant la variable currentUser.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Vérifie si quelqu'un est actuellement connecté.
     */
    public static boolean isLogged() {
        return currentUser != null;
    }

    /**
     * Vérifie si l'utilisateur connecté possède un rôle spécifique (ex: ADMIN).
     * @param roleName Le nom du rôle à tester.
     * @return true si l'utilisateur a ce rôle dans sa liste, false sinon.
     */
    public static boolean hasRole(String roleName) {
        if (!isLogged()) return false;
        
        // On demande à l'objet User s'il possède ce rôle dans sa liste 'roles'.
        return currentUser.hasRole(roleName);
    }

    /**
     * Raccourci pratique pour savoir si l'utilisateur connecté est un Administrateur.
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }
}
