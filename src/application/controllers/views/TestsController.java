package application.controllers.views;

import application.controllers.CatController;
import application.controllers.ExamController;
import application.controllers.UnitController;
import application.controllers.utils.DateStringConverter;
import application.models.Cat;
import application.models.Exam;
import application.models.Unit;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class TestsController implements Initializable {

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
    TextField searchCatTextField;

    @FXML
    TextField searchExamTextField;

    @FXML
    TableColumn<Cat, Unit> catUnitCol;

    @FXML
    TableColumn<Cat, Date> catDateCol;

    @FXML
    TableColumn<Cat, String> catTypeCol;

    @FXML
    TableColumn<Exam, Unit> examUnitCol;

    @FXML
    TableColumn<Exam, Date> examDateCol;

    @FXML
    TableColumn<Cat, Button> editCatActionCol;

    @FXML
    TableColumn<Cat, Button> deleteCatActionCol;

    @FXML
    TableColumn<Exam, Button> editExamActionCol;

    @FXML
    TableColumn<Exam, Button> deleteExamActionCol;

    @FXML
    TableView<Cat> catsTable;

    @FXML
    TableView<Exam> examsTable;

    @FXML
    ComboBox<Unit> catUnitsCombo;

    @FXML
    ComboBox<String> catTypesCombo;

    @FXML
    ComboBox<Unit> examUnitsCombo;

    @FXML
    DatePicker catDatePicker;

    @FXML
    DatePicker examDatePicker;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Cat> catsObservableList = FXCollections.observableArrayList();
        ObservableList<Exam> examsObservableList = FXCollections.observableArrayList();
        ObservableList<Unit> unitsObservableList = FXCollections.observableArrayList();
        ObservableList<String> catTypesObservableList = FXCollections.observableArrayList();
        DateStringConverter stringToDateConverter = new DateStringConverter();

        List<Unit> units = UnitController.getUnits();
        assert units != null;
        unitsObservableList.addAll(units);

        catUnitsCombo.setItems(unitsObservableList);
        examUnitsCombo.setItems(unitsObservableList);

        String[] catTypes = { "Take Away", "Sitting" };
        catTypesObservableList.addAll(catTypes);
        catTypesCombo.setItems(catTypesObservableList);

        if(catUnitCol != null && catDateCol != null && catTypeCol != null && examUnitCol != null && examDateCol != null) {

            List<Cat> cats = CatController.getCats();
            List<Exam> exams = ExamController.getExams();

            assert cats != null;
            catsObservableList.addAll(cats);
            assert exams != null;
            examsObservableList.addAll(exams);

            catUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
            catUnitCol.setCellFactory(ComboBoxTableCell.forTableColumn(unitsObservableList));

            catDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            catDateCol.setSortType(TableColumn.SortType.ASCENDING);

            catTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            catTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(catTypesObservableList));


            examUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
            examUnitCol.setCellFactory(ComboBoxTableCell.forTableColumn(unitsObservableList));

            examDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            examDateCol.setSortType(TableColumn.SortType.ASCENDING);

            Callback<TableColumn<Cat, Button>, TableCell<Cat, Button>> saveCatChangesCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Cat, Button> param) {
                            final TableCell<Cat, Button> cell = new TableCell<>() {

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
                                            Cat cat = getTableView().getItems().get(getIndex());
                                            editCat(cat, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            Callback<TableColumn<Cat, Button>, TableCell<Cat, Button>> deleteCatCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell<Cat, Button> call(final TableColumn<Cat, Button> param) {
                            final TableCell<Cat, Button> cell = new TableCell<>() {

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
                                            Cat cat = getTableView().getItems().get(getIndex());
                                            deleteCat(cat, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };


            Callback<TableColumn<Exam, Button>, TableCell<Exam, Button>> saveExamChangesCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Exam, Button> param) {
                            final TableCell<Exam, Button> cell = new TableCell<>() {

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
                                            Exam exam = getTableView().getItems().get(getIndex());
                                            editExam(exam, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            Callback<TableColumn<Exam, Button>, TableCell<Exam, Button>> deleteExamCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell<Exam, Button> call(final TableColumn<Exam, Button> param) {
                            final TableCell<Exam, Button> cell = new TableCell<>() {

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
                                            Exam exam = getTableView().getItems().get(getIndex());
                                            deleteExam(exam, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };


            editCatActionCol.setCellFactory(saveCatChangesCellFactory);

            deleteCatActionCol.setCellFactory(deleteCatCellFactory);

            editExamActionCol.setCellFactory(saveExamChangesCellFactory);

            deleteExamActionCol.setCellFactory(deleteExamCellFactory);

            catsTable.setItems(catsObservableList);

            examsTable.setItems(examsObservableList);

            searchCat(catsObservableList);
            searchExam(examsObservableList);
        }
    }


    public void addCat(ActionEvent event) throws IOException {
        DateStringConverter dateStringConverter = new DateStringConverter();
        catDatePicker.setConverter(dateStringConverter);

        if (catUnitsCombo.getValue() != null && !dateStringConverter.hasParseError() && !catTypesCombo.getValue().equals("")) {
            Unit unit = catUnitsCombo.getValue();
            Date date = new Date(dateStringConverter.toMills(catDatePicker.getValue()));
            int type = catTypesCombo.getValue().equals("Sitting") ? 1 : 2;

//            Cat cat = new Cat(unit, date, type);
//            cat.save();
            refreshView(event);
        }
    }

    public void addExam(ActionEvent event) throws IOException {
        DateStringConverter dateStringConverter = new DateStringConverter();
        examDatePicker.setConverter(dateStringConverter);

        if (examUnitsCombo.getValue() != null && !dateStringConverter.hasParseError()) {
            Unit unit = examUnitsCombo.getValue();
            Date date = new Date(dateStringConverter.toMills(examDatePicker.getValue()));

//            Exam exam = new Exam(unit, date);
//            exam.save();
            refreshView(event);
        }
    }

    // editing a unit on the cats table
    public void onCatUnitsEditCommit(TableColumn.CellEditEvent<Cat, Unit> catUnitCellEditEvent) {
        Cat cat = catsTable.getSelectionModel().getSelectedItem();
        cat.setUnit(catUnitCellEditEvent.getNewValue());
        System.out.println(cat);
    }

    // editing a cat type on the cats table
    public void onCatTypesEditCommit(TableColumn.CellEditEvent<Cat, String> catStringCellEditEvent) {
        Cat cat = catsTable.getSelectionModel().getSelectedItem();
        cat.setTypeFromString(catStringCellEditEvent.getNewValue());
    }

    // editing a unit on the exams table
    public void onExamUnitsEditCommit(TableColumn.CellEditEvent<Exam, Unit> examUnitCellEditEvent) {
        Exam exam = examsTable.getSelectionModel().getSelectedItem();
        exam.setUnit(examUnitCellEditEvent.getNewValue());
    }

    // edit a cat
    public void editCat(Cat cat, ActionEvent event) {
        CatController.editCat(cat);
        try {
            refreshView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // edit an exam
    public void editExam(Exam exam, ActionEvent event) {
        ExamController.editExam(exam);
        try {
            refreshView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // delete a CAT
    public void deleteCat(Cat cat, ActionEvent event) {
        try {
            setDeleteModal(event, "Delete Cat");
            if(DeleteModalController.isDelete) {
                CatController.deleteCat(cat);
                refreshView(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // delete an exam
    public void deleteExam(Exam exam, ActionEvent event) {
        try {
            setDeleteModal(event, "Delete Exam");
            if(DeleteModalController.isDelete) {
                ExamController.deleteExam(exam);
                refreshView(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // search for a CAT
    public void searchCat(ObservableList<Cat> unitsObservableList) {

        FilteredList<Cat> filteredCats = new FilteredList<>(unitsObservableList, b -> true);

        // adding a change listener to the search text field
        searchCatTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredCats.setPredicate(catItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (catItems.getUnitObject().getName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (catItems.getUnitObject().getLecturer().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (catItems.getUnitObject().getCode().contains(searchKeyword)) {
                return true;
            } else return catItems.getType().contains(searchKeyword);

        }));

        SortedList<Cat> sortedData = new SortedList<>(filteredCats);

        sortedData.comparatorProperty().bind(catsTable.comparatorProperty());

        catsTable.setItems(sortedData);

    }

    // search for an exam
    public void searchExam(ObservableList<Exam> unitsObservableList) {

        FilteredList<Exam> filteredExams = new FilteredList<>(unitsObservableList, b -> true);

        // adding a change listener to the search text field
        searchExamTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredExams.setPredicate(examItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (examItems.getUnitObject().getName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (examItems.getUnitObject().getLecturer().toLowerCase().contains(searchKeyword)) {
                return true;
            } else return examItems.getUnitObject().getCode().contains(searchKeyword);

        }));

        SortedList<Exam> sortedData = new SortedList<>(filteredExams);

        sortedData.comparatorProperty().bind(examsTable.comparatorProperty());

        examsTable.setItems(sortedData);

    }

    // refresh a page
    public void refreshView(ActionEvent event) throws IOException {
        setView("Tests.fxml", event);
    }

    // switch views
    public void changeView(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        switch (clickedButton.getId()) {
            case "homeButton" -> setView("Home.fxml", event);
            case "unitsButton" -> setView("Units.fxml", event);
            case "remindersButton" -> setView("Reminders.fxml", event);
            case "submissionsButton" -> setView("Submissions.fxml", event);
            case "loungeButton" -> setView("Lounge.fxml", event);
            default -> setView("Tests.fxml", event);
        }

    }

    // logout a user
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

}
