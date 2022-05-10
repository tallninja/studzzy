package application.controllers.views;

import application.controllers.utils.DateStringConverter;
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
import java.sql.Date;

public class SignupController {

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    @FXML
    TextField emailField;

    @FXML
    TextField passwordField;

    @FXML
    TextField universityField;

    @FXML
    TextField regField;

    @FXML
    DatePicker startSemDatePicker;

    @FXML
    DatePicker endSemDatePicker;

    @FXML
    Button loginButton, createAccountButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void createAccount(ActionEvent event) throws IOException {

        if (checkAllFieldsHaveValues()) {

            DateStringConverter stringToDateFormatter = new DateStringConverter();
//            System.out.println(startSemDatePicker.getValue().toString());
//            System.out.println(endSemDatePicker.getValue().toString());

            Date startSemDate = new Date(stringToDateFormatter.toMills(startSemDatePicker.getValue()));
            Date endSemDate = new Date(stringToDateFormatter.toMills(endSemDatePicker.getValue()));

            User user = new User(firstNameField.getText(), lastNameField.getText(), regField.getText(), universityField.getText(),
                    startSemDate, endSemDate, emailField.getText(), passwordField.getText());
            user.save();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../views/Login.fxml"));
            root = loader.load();
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill out all the fields !", ButtonType.CLOSE);
            alert.showAndWait();
        }

    }

    private boolean checkAllFieldsHaveValues() {

        DateStringConverter dateStringConverter = new DateStringConverter();
        startSemDatePicker.setConverter(dateStringConverter);
        endSemDatePicker.setConverter(dateStringConverter);

        return !firstNameField.getText().equals("") && !lastNameField.getText().equals("") &&
                !emailField.getText().equals("") && !passwordField.getText().equals("") &&
                !universityField.getText().equals("") && !regField.getText().equals("") &&
                !dateStringConverter.hasParseError();
    }

    public void loginUser(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Login.fxml"));
        root = loader.load();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

    }

    public static void signUp(String firstName, String lastName, String registrationNumber, String university,
                              Date startSemDate, Date endSemDate, String email, String password) {

        User user = new User(firstName, lastName, registrationNumber, university, new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()), email, password);

    }

}
