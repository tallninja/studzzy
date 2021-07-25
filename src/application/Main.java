package application;

import application.models.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Database.open();

        Parent root = FXMLLoader.load(getClass().getResource("./views/Login.fxml"));
        primaryStage.setTitle("Studzzy");
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.show();

        Database.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
