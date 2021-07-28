package application.controllers.views;

import application.controllers.ReminderController;
import application.controllers.UnitController;
import application.controllers.utils.DateStringConverter;
import application.models.Reminder;
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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RemindersController implements Initializable {

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
    TableView<Reminder> remindersTable;

    @FXML
    TableColumn<Reminder, String> reminderDescriptionCol;

    @FXML
    TableColumn<Reminder, Date> reminderDateCol;

    @FXML
    TableColumn<Reminder, Button> editReminderAction;

    @FXML
    TableColumn<Reminder, Button> deleteReminderAction;

    @FXML
    TextField searchTextField;

    @FXML
    TextArea reminderDescriptionTextArea;

    @FXML
    DatePicker reminderDatePicker;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Reminder> remindersObservableList = FXCollections.observableArrayList();
        List<Reminder> reminders = ReminderController.getReminders();
        assert reminders != null;
        remindersObservableList.addAll(reminders);

        if(reminderDescriptionCol != null && reminderDateCol != null) {

            reminderDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            reminderDescriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());

            reminderDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

            Callback<TableColumn<Reminder, Button>, TableCell<Reminder, Button>> saveChangesCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Reminder, Button> param) {
                            final TableCell<Reminder, Button> cell = new TableCell<>() {

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
                                            Reminder reminder = getTableView().getItems().get(getIndex());
                                            editReminder(reminder, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            Callback<TableColumn<Reminder, Button>, TableCell<Reminder, Button>> deleteCellFactory
                    = //
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Reminder, Button> param) {
                            final TableCell<Reminder, Button> cell = new TableCell<>() {

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
                                            Reminder reminder = getTableView().getItems().get(getIndex());
                                            deleteReminder(reminder, event);
                                        });
                                        setGraphic(actionButton);
                                    }
                                    setText(null);
                                }
                            };
                            return cell;
                        }
                    };

            editReminderAction.setCellFactory(saveChangesCellFactory);
            deleteReminderAction.setCellFactory(deleteCellFactory);

            remindersTable.setItems(remindersObservableList);

            searchReminder(remindersObservableList);
        }

    }


    public void addReminder(ActionEvent event) {
        setModal("CreateReminderModal.fxml", "Create Reminder", event);
    }


    public void createReminder(ActionEvent event) throws  IOException{

        DateStringConverter dateStringConverter = new DateStringConverter();

        if (!reminderDescriptionTextArea.getText().equals("") && !reminderDatePicker.getValue().toString().equals("")) {

            String description = reminderDescriptionTextArea.getText();
            Date date = new Date(dateStringConverter.toMills(reminderDatePicker.getValue()));

            Reminder reminder = new Reminder(description, date);
            reminder.save();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }



    }

    public void onReminderDescriptionEditCommit(CellEditEvent<Reminder, String> reminderStringCellEditEvent) {
        Reminder reminder = remindersTable.getSelectionModel().getSelectedItem();
        reminder.setDescription(reminderStringCellEditEvent.getNewValue());
    }

    public void editReminder(Reminder reminder, ActionEvent event) {
        ReminderController.editReminder(reminder);
        try {
            refreshView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteReminder(Reminder reminder, ActionEvent event) {
        try {
            setDeleteModal(event, "Delete Reminder");
            if(DeleteModalController.isDelete) {
                ReminderController.deleteReminder(reminder);
                refreshView(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchReminder(ObservableList<Reminder> remindersObservableList) {

        FilteredList<Reminder> filteredReminders = new FilteredList<>(remindersObservableList, b -> true);

        // adding a change listener to the search text field
        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> filteredReminders.setPredicate(reminderItems -> {

            // if the search-field is empty then return all the values
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            return reminderItems.getDescription().toLowerCase().contains(searchKeyword);

        }));

        SortedList<Reminder> sortedData = new SortedList<>(filteredReminders);

        sortedData.comparatorProperty().bind(remindersTable.comparatorProperty());

        remindersTable.setItems(sortedData);

    }

    public void refreshView(ActionEvent event) throws IOException {
        setView("Reminders.fxml", event);
    }

    public void changeView(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        switch (clickedButton.getId()) {
            case "homeButton" -> setView("Home.fxml", event);
            case "unitsButton" -> setView("Units.fxml", event);
            case "testsButton" -> setView("Tests.fxml", event);
            case "submissionsButton" -> setView("Submissions.fxml", event);
            case "loungeButton" -> setView("Lounge.fxml", event);
            default -> setView("Reminders.fxml", event);
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

    public void setModal(String resource, String modalTitle, ActionEvent event) {

        String fxmlPath = String.format("../../views/modals/%s", resource);
        Parent root = null;
        try {
            root = FXMLLoader.load(RemindersController.class.getResource(fxmlPath));
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
