package application.controllers.views;

import application.controllers.UnitController;
import application.models.Unit;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UnitsController implements Initializable {

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
    TableColumn<Unit, String> unitNameCol;

    @FXML
    TableColumn<Unit, String> unitCodeCol;

    @FXML
    TableColumn<Unit, String> lecturerCol;

    @FXML
    TableColumn<Unit, Integer> pagesCol;

    @FXML
    TextField searchTextField;

    @FXML
    DialogPane dialogPane;

    ObservableList<Unit> unitsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Unit> units = UnitController.getUnits();

        assert  units != null;
        unitsObservableList.addAll(units);

        unitNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        lecturerCol.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        pagesCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        unitsTable.setItems(unitsObservableList);

        FilteredList<Unit> filteredUnits = new FilteredList<>(unitsObservableList, b -> true);

        // adding a change listener to the search text field
        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredUnits.setPredicate(unitItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if(unitItems.getName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (unitItems.getLecturer().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (unitItems.getCode().contains(searchKeyword)) {
                return true;
            } else {
                return false;
            }

        }));

        SortedList<Unit> sortedData = new SortedList<>(filteredUnits);

        sortedData.comparatorProperty().bind(unitsTable.comparatorProperty());

        unitsTable.setItems(sortedData);
    }

    public void addUnit(ActionEvent event) {

    }

    public void deleteUnit(ActionEvent event) {

    }

    public void refreshView(ActionEvent event) throws IOException {
        setView("Units.fxml", event);
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
