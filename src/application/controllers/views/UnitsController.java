package application.controllers.views;

import application.controllers.UnitController;
import application.models.Unit;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UnitsController {

    @FXML
    Button homeButton;

    @FXML
    Button unitsButton;

    @FXML
    Button remindersButton;

    @FXML
    Button testsButton;

    @FXML
    Button submissionsButton;

    @FXML
    Button loungeButton;

    @FXML
    Button logoutButton;

    @FXML
    TableView<Unit> unitsTable;

    @FXML
    VBox vBox;

    public void populateTable(ActionEvent event) {
        ObservableList<Unit> units = FXCollections.observableArrayList(UnitController.getUnits());

        TableColumn<Unit, String> unitNameCol = new TableColumn<>("Unit Name");
        TableColumn<Unit, String> unitCodeCol = new TableColumn<>("Unit Code");
        TableColumn<Unit, String> unitLecCol = new TableColumn<>("Lecturer");
        TableColumn<Unit, Integer> unitPagesCol = new TableColumn<>("Pages");

        unitsTable.getColumns().addAll(unitNameCol, unitCodeCol, unitLecCol, unitPagesCol);

        vBox.getChildren().add(unitsTable);

    }

    public void changeView(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        switch (clickedButton.getId()) {
            case "homeButton" -> setView("Home.fxml", event);
            case "remindersButton" -> setView("Reminders.fxml", event);
            case "testsButton" -> setView("Tests.fxml", event);
            case "submissionsButton" -> setView("Submissions.fxml", event);
            case "loungeButton" -> setView("Lounge.fxml", event);
            default -> setView("Units.fxml", event);
        }
    }

    public void logoutUser(ActionEvent event) throws IOException {
        setView("Login.fxml", event);
    }

    public void setView(String resource, ActionEvent event) throws IOException {

        Stage stage;
        Scene scene;
        Parent root;

        String fxmlPath = String.format("../../views/%s", resource);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        root = loader.load();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
