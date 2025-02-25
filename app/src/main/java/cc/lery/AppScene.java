package cc.lery;

    

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppScene extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/home_page.fxml"));
        primaryStage.setTitle("ERP HP/AP");
        primaryStage.setScene(new Scene(root,1250, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
