package application.controllers;

import application.controllers.utils.Password;
import application.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton;

    @FXML
    Button createAccountButton;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void loginUser(ActionEvent event) throws IOException {

        if (!emailField.getText().equals("") && !passwordField.getText().equals("")) {

            if (login(emailField.getText(), passwordField.getText())) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Dashboard.fxml"));
                root = loader.load();
                scene = new Scene(root);
                stage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setFullScreen(false);
                stage.setResizable(false);
                stage.show();


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Username or Password !",
                                        ButtonType.CLOSE);
                alert.showAndWait();
                emailField.setText("");
                passwordField.setText("");
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter email and password !",
                                    ButtonType.CLOSE);
            alert.showAndWait();

        }

    }

    public void createAccount(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CreateAccount.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.show();

    }

    public static boolean login(String email, String password) {

        User user = UserController.getUser(email);

        if(user != null) {

            return Password.verify(password, user.getPassword());

        } else {
            return false;
        }
    }
}
