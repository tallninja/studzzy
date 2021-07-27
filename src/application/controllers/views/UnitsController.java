package application.controllers.views;

import application.controllers.UnitController;
import application.models.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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
    TableColumn<Unit, Button> editActionCol;

    @FXML
    TableColumn<Unit, Button> deleteActionCol;

    @FXML
    TextField searchTextField;


    @FXML
    TextField unitNameField;

    @FXML
    TextField unitCodeField;

    @FXML
    TextField lecturerField;

    @FXML
    Spinner<Integer> numberOfPagesField;

    @FXML
    Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Unit> unitsObservableList = FXCollections.observableArrayList();

        if(unitNameCol != null && unitCodeCol != null && lecturerCol != null && pagesCol != null) {
            List<Unit> units = UnitController.getUnits();

            assert units != null;
            unitsObservableList.addAll(units);

            Callback<TableColumn<Unit, String>, TableCell<Unit, String>> editCellFactory =
                    p -> new EditingCell();

            unitNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            unitNameCol.setCellFactory(editCellFactory);
            unitNameCol.setOnEditCommit(
                    t -> t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setName(t.getNewValue())
            );

            unitCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
            unitCodeCol.setCellFactory(editCellFactory);
            unitCodeCol.setOnEditCommit(
                    t -> t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setCode(t.getNewValue())
            );

            lecturerCol.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
            lecturerCol.setCellFactory(editCellFactory);
            lecturerCol.setOnEditCommit(
                    t -> t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setLecturer(t.getNewValue())
            );

            pagesCol.setCellValueFactory(new PropertyValueFactory<>("pages"));

            Callback<TableColumn<Unit, Button>, TableCell<Unit, Button>> saveChangesCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Unit, Button> param) {
                            final TableCell<Unit, Button> cell = new TableCell<>() {

                                @Override
                                public void updateItem(Button item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                    } else {

                                        Button actionButton = new Button("Save");
                                        actionButton.getStyleClass().add("cta-button");
                                        actionButton.setMaxWidth(Double.MAX_VALUE);

                                        actionButton.setOnAction(event -> {
                                            Unit unit = getTableView().getItems().get(getIndex());
                                            editUnit(unit, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            Callback<TableColumn<Unit, Button>, TableCell<Unit, Button>> deleteCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Unit, Button> param) {
                            final TableCell<Unit, Button> cell = new TableCell<>() {

                                @Override
                                public void updateItem(Button item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                    } else {

                                        Button actionButton = new Button("Delete");
                                        actionButton.getStyleClass().add("cta-button");
                                        actionButton.setMaxWidth(Double.MAX_VALUE);

                                        actionButton.setOnAction(event -> {
                                            Unit unit = getTableView().getItems().get(getIndex());
                                            deleteUnit(unit, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            editActionCol.setCellFactory(saveChangesCellFactory);

            deleteActionCol.setCellFactory(deleteCellFactory);

            unitsTable.setItems(unitsObservableList);

            searchUnit(unitsObservableList);
        }

        if (numberOfPagesField != null) {
            SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000);
            spinnerValueFactory.setValue(0);
            numberOfPagesField.setValueFactory(spinnerValueFactory);
        }
    }

    public void addUnit(ActionEvent event) throws IOException {
        setModal("UnitModal.fxml", "Create Unit", event);
    }

    public void createUnit(ActionEvent event) {
        String unitName = unitNameField.getText();
        String unitCode = unitCodeField.getText();
        String unitLecturer = lecturerField.getText();
        int numOfPages = numberOfPagesField.getValue();

        if (!unitName.equals("") && !unitCode.equals("") && !unitLecturer.equals("")) {
            Unit unit = new Unit(unitName, unitCode, unitLecturer, numOfPages);
            unit.save();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void editUnit(Unit unit, ActionEvent event) {
        UnitController.editUnit(unit);
    }

    public void deleteUnit(Unit unit, ActionEvent event) {
        try {
            setDeleteModal(event, "Delete Unit");
            if(DeleteModalController.isDelete) {
                UnitController.deleteUnit(unit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchUnit(ObservableList<Unit> unitsObservableList) {

        FilteredList<Unit> filteredUnits = new FilteredList<>(unitsObservableList, b -> true);

        // adding a change listener to the search text field
        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredUnits.setPredicate(unitItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (unitItems.getName().toLowerCase().contains(searchKeyword)) {
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

    // creates a popup window
    public void setModal(String resource, String modalTitle, ActionEvent event) {

        String fxmlPath = String.format("../../views/modals/%s", resource);
        Parent root = null;
        try {
            root = FXMLLoader.load(UnitsController.class.getResource(fxmlPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(modalTitle);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(( (Node) event.getSource() ).getScene().getWindow());
        stage.show();

    }

    // creates a popup window
    public void setDeleteModal(ActionEvent event, String modalTitle) throws IOException {

        String fxmlPath = "../../views/modals/DeleteModal.fxml";

        Parent root = FXMLLoader.load(UnitsController.class.getResource(fxmlPath));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(modalTitle);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(( (Node) event.getSource() ).getScene().getWindow());
        stage.showAndWait();

    }

    static class EditingCell extends TableCell<Unit, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.getStyleClass().add("cta-text-field");
            textField.focusedProperty().addListener((arg0, arg1, arg2) -> {
                if (!arg2) {
                    commitEdit(textField.getText());
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}
