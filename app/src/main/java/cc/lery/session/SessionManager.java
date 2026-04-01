package cc.lery.session;

import cc.lery.model.User;

/**
 * Le SessionManager est une classe qui permet de gérer l'état de la session de l'utilisateur.
 * Elle permet de savoir qui est connecté, son rôle, et de gérer sa déconnexion.
 */
public class SessionManager {

    /**
     * Variable statique qui contient l'utilisateur connecté pendant toute la durée de l'exécution du programme.
     * Cette variable est accessible de partout dans l'application via les méthodes statiques ci-dessous.
     */
    private static User currentUser;

    /**
     * Méthode appelée lors d'une connexion réussie (dans LoginController ou UserService.validateUser).
     * Stocke les informations de l'utilisateur (incluant son rôle) dans la mémoire de l'application.
     * @param user L'objet User récupéré de la base de données.
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Retourne l'utilisateur actuellement connecté.
     * Utilisé pour récupérer ses informations (nom, rôle, etc.) n'importe où dans le code.
     * @return L'objet User connecté, ou null si personne n'est connecté.
     */
    public static User getUser() {
        return currentUser;
    }

    /**
     * Réinitialise la session.
     * Méthode appelée lors du clic sur le bouton "Déconnexion".
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Vérifie si un utilisateur est actuellement authentifié.
     * @return true si une session est active, false sinon.
     */
    public static boolean isLogged() {
        return currentUser != null;
    }
}
