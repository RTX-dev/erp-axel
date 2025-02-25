package cc.lery;

    

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer une BorderPane comme layout principal
        BorderPane root = new BorderPane();

        // Créer une MenuBar
        MenuBar menuBar = new MenuBar();

        // Créer des Menus
        Menu fileMenu = new Menu("Fichier");
        Menu editMenu = new Menu("Édition");
        Menu helpMenu = new Menu("Aide");

        // Créer des MenuItems pour le menu Fichier
        MenuItem newItem = new MenuItem("Nouveau");
        MenuItem openItem = new MenuItem("Ouvrir");
        MenuItem exitItem = new MenuItem("Quitter");

        // Ajouter les MenuItems au menu Fichier
        fileMenu.getItems().addAll(newItem, openItem, exitItem);

        // Ajouter les Menus à la MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        // Ajouter la MenuBar au layout principal
        root.setTop(menuBar);

        // Créer une Scene et ajouter le layout principal
        Scene scene = new Scene(root, 400, 300);

        // Configurer la Stage
        primaryStage.setTitle("Exemple de MenuBar JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
