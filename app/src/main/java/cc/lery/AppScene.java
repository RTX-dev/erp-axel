package cc.lery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * AppScene : Gère le démarrage de l'interface graphique (GUI) avec JavaFX.
 * Cette classe hérite de 'Application', ce qui permet de lancer des fenêtres.
 */
public class AppScene extends Application {

    /**
     * Méthode appelée automatiquement par JavaFX lors du lancement.
     * C'est ici qu'on définit la première fenêtre (Stage) à afficher.
     * 
     * @param primaryStage La fenêtre principale créée par JavaFX.
     * @throws Exception Si le fichier FXML n'est pas trouvé.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charge le fichier FXML qui contient le design de la page de connexion.
        Parent root = FXMLLoader.load(getClass().getResource("/view/page-connexion.fxml"));
        
        // Définit le titre de la fenêtre.
        primaryStage.setTitle("ERP HP/AP");
        
        // Définit la scène (le contenu) de la fenêtre avec une taille par défaut.
        primaryStage.setScene(new Scene(root, 640, 400));
        
        // Affiche enfin la fenêtre à l'écran.
        primaryStage.show();
    }
}
