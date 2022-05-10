package application.controllers.views;

import application.controllers.ReportController;
import application.controllers.AssignmentController;
import application.controllers.UnitController;
import application.controllers.UserController;
import application.controllers.utils.DateStringConverter;
import application.models.Report;
import application.models.Assignment;
import application.models.Unit;
import application.models.User;
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

public class SubmissionsController implements Initializable {

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
    TextField searchReportTextField;

    @FXML
    TextField searchAssignmentTextField;

    @FXML
    TableColumn<Report, Unit> reportUnitCol;

    @FXML
    TableColumn<Report, Date> reportDateCol;

    @FXML
    TableColumn<Report, String> reportTypeCol;

    @FXML
    TableColumn<Assignment, Unit> assignmentUnitCol;

    @FXML
    TableColumn<Assignment, Date> assignmentDateCol;

    @FXML
    TableColumn<Report, String> assignmentTypeCol;

    @FXML
    TableColumn<Report, Button> editReportActionCol;

    @FXML
    TableColumn<Report, Button> deleteReportActionCol;

    @FXML
    TableColumn<Assignment, Button> editAssignmentActionCol;

    @FXML
    TableColumn<Assignment, Button> deleteAssignmentActionCol;

    @FXML
    TableView<Report> reportsTable;

    @FXML
    TableView<Assignment> assignmentsTable;

    @FXML
    ComboBox<Unit> reportUnitsCombo;

    @FXML
    ComboBox<String> reportTypesCombo;

    @FXML
    ComboBox<Unit> assignmentUnitsCombo;

    @FXML
    ComboBox<String> assignmentTypesCombo;

    @FXML
    DatePicker reportDatePicker;

    @FXML
    DatePicker assignmentDatePicker;


    private final User user = UserController.getUser(SessionController.getSession());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Report> reportsObservableList = FXCollections.observableArrayList();
        ObservableList<Assignment> assignmentsObservableList = FXCollections.observableArrayList();
        ObservableList<Unit> unitsObservableList = FXCollections.observableArrayList();
        ObservableList<String> reportTypesObservableList = FXCollections.observableArrayList();
        ObservableList<String> assignmentTypesObservableList = FXCollections.observableArrayList();
        DateStringConverter stringToDateConverter = new DateStringConverter();

        List<Unit> units = UnitController.getUnits(user);
        assert units != null;
        unitsObservableList.addAll(units);

        reportUnitsCombo.setItems(unitsObservableList);
        assignmentUnitsCombo.setItems(unitsObservableList);

        String[] submissionTypes = { "Group", "Individual" };
        reportTypesObservableList.addAll(submissionTypes);
        reportTypesCombo.setItems(reportTypesObservableList);
        assignmentTypesObservableList.addAll(submissionTypes);
        assignmentTypesCombo.setItems(assignmentTypesObservableList);


        if(reportUnitCol != null && reportDateCol != null && reportTypeCol != null &&
                assignmentUnitCol != null && assignmentDateCol != null && assignmentTypeCol != null) {

            List<Report> reports = ReportController.getReports(user);
            List<Assignment> assignments = AssignmentController.getAssignments(user);

            assert reports != null;
            reportsObservableList.addAll(reports);
            assert assignments != null;
            assignmentsObservableList.addAll(assignments);

            reportUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
            reportUnitCol.setCellFactory(ComboBoxTableCell.forTableColumn(unitsObservableList));

            reportDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            reportDateCol.setSortType(TableColumn.SortType.ASCENDING);

            reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            reportTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(reportTypesObservableList));


            assignmentUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
            assignmentUnitCol.setCellFactory(ComboBoxTableCell.forTableColumn(unitsObservableList));

            assignmentDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            assignmentDateCol.setSortType(TableColumn.SortType.ASCENDING);

            assignmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            assignmentTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(reportTypesObservableList));

            Callback<TableColumn<Report, Button>, TableCell<Report, Button>> saveReportChangesCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Report, Button> param) {
                            final TableCell<Report, Button> cell = new TableCell<>() {

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
                                            Report report = getTableView().getItems().get(getIndex());
                                            editReport(report, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            Callback<TableColumn<Report, Button>, TableCell<Report, Button>> deleteReportCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell<Report, Button> call(final TableColumn<Report, Button> param) {
                            final TableCell<Report, Button> cell = new TableCell<>() {

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
                                            Report report = getTableView().getItems().get(getIndex());
                                            deleteReport(report, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };


            Callback<TableColumn<Assignment, Button>, TableCell<Assignment, Button>> saveAssignmentChangesCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Assignment, Button> param) {
                            final TableCell<Assignment, Button> cell = new TableCell<>() {

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
                                            Assignment assignment = getTableView().getItems().get(getIndex());
                                            editAssignment(assignment, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            Callback<TableColumn<Assignment, Button>, TableCell<Assignment, Button>> deleteAssignmentCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell<Assignment, Button> call(final TableColumn<Assignment, Button> param) {
                            final TableCell<Assignment, Button> cell = new TableCell<>() {

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
                                            Assignment assignment = getTableView().getItems().get(getIndex());
                                            deleteAssignment(assignment, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };


            editReportActionCol.setCellFactory(saveReportChangesCellFactory);

            deleteReportActionCol.setCellFactory(deleteReportCellFactory);

            editAssignmentActionCol.setCellFactory(saveAssignmentChangesCellFactory);

            deleteAssignmentActionCol.setCellFactory(deleteAssignmentCellFactory);

            reportsTable.setItems(reportsObservableList);

            assignmentsTable.setItems(assignmentsObservableList);

            searchReport(reportsObservableList);
            searchAssignment(assignmentsObservableList);
        }
    }


    public void addReport(ActionEvent event) throws IOException {
        DateStringConverter dateStringConverter = new DateStringConverter();
        reportDatePicker.setConverter(dateStringConverter);

        if (reportUnitsCombo.getValue() != null && !dateStringConverter.hasParseError() && !reportTypesCombo.getValue().equals("")) {
            Unit unit = reportUnitsCombo.getValue();
            Date date = new Date(dateStringConverter.toMills(reportDatePicker.getValue()));
            int type = reportTypesCombo.getValue().equals("Individual") ? 1 : 2;

            Report report = new Report(user, unit, date, type);
            report.save();
            refreshView(event);
        }
    }

    public void addAssignment(ActionEvent event) throws IOException {
        DateStringConverter dateStringConverter = new DateStringConverter();
        assignmentDatePicker.setConverter(dateStringConverter);

        if (assignmentUnitsCombo.getValue() != null && !dateStringConverter.hasParseError()) {
            Unit unit = assignmentUnitsCombo.getValue();
            Date date = new Date(dateStringConverter.toMills(assignmentDatePicker.getValue()));
            int type = assignmentTypesCombo.getValue().equals("Individual") ? 1 : 2;

            Assignment assignment = new Assignment(user, unit, date, type);
            assignment.save();
            refreshView(event);
        }
    }

    // editing a unit on the reports table
    public void onReportUnitsEditCommit(TableColumn.CellEditEvent<Report, Unit> reportUnitCellEditEvent) {
        Report report = reportsTable.getSelectionModel().getSelectedItem();
        report.setUnit(reportUnitCellEditEvent.getNewValue());
        System.out.println(report);
    }

    // editing a report type on the reports table
    public void onReportTypesEditCommit(TableColumn.CellEditEvent<Report, String> reportStringCellEditEvent) {
        Report report = reportsTable.getSelectionModel().getSelectedItem();
        report.setTypeFromString(reportStringCellEditEvent.getNewValue());
    }

    // editing a unit on the assignments table
    public void onAssignmentUnitsEditCommit(TableColumn.CellEditEvent<Assignment, Unit> assignmentUnitCellEditEvent) {
        Assignment assignment = assignmentsTable.getSelectionModel().getSelectedItem();
        assignment.setUnit(assignmentUnitCellEditEvent.getNewValue());
    }

    // editing an assignment type on the reports table
    public void onAssignmentTypesEditCommit(TableColumn.CellEditEvent<Report, String> assignmentStringCellEditEvent) {
        Assignment assignment = assignmentsTable.getSelectionModel().getSelectedItem();
        assignment.setTypeFromString(assignmentStringCellEditEvent.getNewValue());
    }

    // edit a report
    public void editReport(Report report, ActionEvent event) {
        ReportController.editReport(report);
        try {
            refreshView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // edit an assignment
    public void editAssignment(Assignment assignment, ActionEvent event) {
        AssignmentController.editAssignment(assignment);
        try {
            refreshView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // delete a CAT
    public void deleteReport(Report report, ActionEvent event) {
        try {
            setDeleteModal(event, "Delete Report");
            if(DeleteModalController.isDelete) {
                ReportController.deleteReport(report);
                refreshView(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // delete an assignment
    public void deleteAssignment(Assignment assignment, ActionEvent event) {
        try {
            setDeleteModal(event, "Delete Assignment");
            if(DeleteModalController.isDelete) {
                AssignmentController.deleteAssignment(assignment);
                refreshView(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // search for a CAT
    public void searchReport(ObservableList<Report> unitsObservableList) {

        FilteredList<Report> filteredReports = new FilteredList<>(unitsObservableList, b -> true);

        // adding a change listener to the search text field
        searchReportTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredReports.setPredicate(reportItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (reportItems.getUnitObject().getName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (reportItems.getUnitObject().getLecturer().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (reportItems.getUnitObject().getCode().contains(searchKeyword)) {
                return true;
            } else return reportItems.getType().contains(searchKeyword);

        }));

        SortedList<Report> sortedData = new SortedList<>(filteredReports);

        sortedData.comparatorProperty().bind(reportsTable.comparatorProperty());

        reportsTable.setItems(sortedData);

    }

    // search for an assignment
    public void searchAssignment(ObservableList<Assignment> unitsObservableList) {

        FilteredList<Assignment> filteredAssignments = new FilteredList<>(unitsObservableList, b -> true);

        // adding a change listener to the search text field
        searchAssignmentTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredAssignments.setPredicate(assignmentItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (assignmentItems.getUnitObject().getName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (assignmentItems.getUnitObject().getLecturer().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (assignmentItems.getUnitObject().getCode().contains(searchKeyword)) {
                return true;
            } else return assignmentItems.getType().contains(searchKeyword);

        }));

        SortedList<Assignment> sortedData = new SortedList<>(filteredAssignments);

        sortedData.comparatorProperty().bind(assignmentsTable.comparatorProperty());

        assignmentsTable.setItems(sortedData);

    }

    // refresh a page
    public void refreshView(ActionEvent event) throws IOException {
        setView("Submissions.fxml", event);
    }

    // switch views
    public void changeView(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        switch (clickedButton.getId()) {
            case "homeButton" -> setView("Home.fxml", event);
            case "unitsButton" -> setView("Units.fxml", event);
            case "remindersButton" -> setView("Reminders.fxml", event);
            case "testsButton" -> setView("Tests.fxml", event);
            case "loungeButton" -> setView("Lounge.fxml", event);
            default -> setView("Submissions.fxml", event);
        }

    }

    // logout a user
    public void logoutUser(ActionEvent event) throws IOException {
        SessionController.deleteSession();
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
