package application;

import application.models.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        if(Database.open()) {

            Parent root = FXMLLoader.load(getClass().getResource("./views/Login.fxml"));
            Scene scene = new Scene(root);

            Image appIcon = new Image("studzzy-icon.png");
            primaryStage.getIcons().add(appIcon);

            primaryStage.setTitle("Studzzy");
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setFullScreen(false);
            primaryStage.setResizable(false);
            primaryStage.show();

            Database.close();
        } else {
            System.out.println("Please make sure the database is up and running !");
            System.exit(1);
        }



    }

    public static void main(String[] args) {
        launch(args);
    }
}
