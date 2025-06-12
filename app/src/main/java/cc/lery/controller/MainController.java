package cc.lery.controller;



import cc.lery.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TextField loginfield;
    @FXML
    private PasswordField passwordfield;
    @FXML
    private Text errormsg;


    /*@FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un ficher");

        //Définir un filtre pour n'afficher que certain types de fichier (ex: image)
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers texte(*.txt)", "*.txt")
        );

        // Ouvrir la boîte de dialogue et récupérer le fichier sélectionné
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Aucun ficher sélectionné");
        }
    }*/
    @FXML
    private void handleConnectionButun(ActionEvent event){
        String email = loginfield.getText();
        String password = passwordfield.getText();
        UserService userService = new UserService();
        if(userService.validateUser(email, password)){
            //cherger le dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                    try{ Parent root = loader.load();

                    //Créer une nouvelle scène avec le contenu de la deuxième page
                    Scene scene = new Scene(root);

                    //Obtenir la scène actuelle à partir de l'événement
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    //Définir la nouvelle scène sur le stage
                    stage.setScene(scene);
                    stage.show();
                    }catch (Exception e){
                        System.out.println("Erreur de chargement"+e.getMessage());
                    }

        }else{
            //Message d'erreur
            errormsg.setText("Le mot de passse ou le mail est incorrect");
            
        }
    }
    

    @FXML
    public void handleDashboard(ActionEvent event) {
        try {
            //affichage page Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent root = loader.load();

            //Créer une nouvelle scène avec le contenu de la deuxième page
            Scene scene = new Scene(root);

            //Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //En decomposant le code ci-dessus on pourrait écrire :
            //Node node = (Node) event.getSource();
            //Scene scene = node.getScene();
            //Stage stage = (Stage) scene.getWindow();

            //Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
