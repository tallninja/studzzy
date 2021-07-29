package application.controllers.views;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoungeController implements Initializable {

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
    Button ytButton;

    @FXML
    Button fbButton;

    @FXML
    Button twButton;

    @FXML
    Button lnButton;

    @FXML
    Button glButton;

    @FXML
    Button backButton;

    @FXML
    Button forwardButton;

    @FXML
    TextField webSearchField;

    @FXML
    WebView webView;

    WebEngine engine;

    String homePage;

    WebHistory history;

    double webZoom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        engine = webView.getEngine();
        homePage = "www.google.com";
        webSearchField.setText(homePage);
        webZoom = 1;
        webView.setZoom(webZoom);
        loadPage();

    }

    public void loadPage() {
        engine.load("http://" + webSearchField.getText());
    }

    public  void searchPage(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)) {
            loadPage();
        }
    }

    public void refreshPage() {
        engine.reload();
    }

    public void zoomIn() {
        webZoom += 0.25;
        webView.setZoom(webZoom);
    }

    public void zoomOut() {
        webZoom -= 0.25;
        webView.setZoom(0.75);
    }

    public void goTo(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();

        String youTubeUrl = "https://www.youtube.com";
        String gitHubUrl = "https://www.github.com";
        String twitterUrl = "https://www.twitter.com";
        String linkedinUrl = "https://www.linkedin.com";
        String googleUrl = "https://www.google.com";

        switch (clickedButton.getId()) {
            case "ytButton" -> {
                engine.load(youTubeUrl);
                webSearchField.setText(youTubeUrl);
            }
            case "ghButton" -> {
                engine.load(gitHubUrl);
                webSearchField.setText(gitHubUrl);
            }
            case "twButton" -> {
                engine.load(twitterUrl);
                webSearchField.setText(twitterUrl);
            }
            case "lnButton" -> {
                engine.load(linkedinUrl);
                webSearchField.setText(linkedinUrl);
            }
            default -> {
                engine.load(googleUrl);
                webSearchField.setText(googleUrl);
            }
        }

    }

    public void displayHistory() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();

        for(WebHistory.Entry entry : entries) {
            System.out.println(entry);
        }
    }

    public void clearHistory() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();

        try {
            entries.removeAll();
            System.out.println("History Creared Successfully !");
        } catch (Exception e) {
            System.out.println("Cannot clear history !");
        }
    }

    public void goForward() {

        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();

        try {
            history.go(1);
            webSearchField.setText(entries.get(history.getCurrentIndex()).getUrl());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Cannot go Forward !");
        }

    }

    public void goBack() {

        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();

        try {
            history.go(-1);
            webSearchField.setText(entries.get(history.getCurrentIndex()).getUrl());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Cannot go back !");
        }

    }


    public void changeView(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        switch (clickedButton.getId()) {
            case "homeButton" -> setView("Home.fxml", event);
            case "unitsButton" -> setView("Units.fxml", event);
            case "remindersButton" -> setView("Reminders.fxml", event);
            case "testsButton" -> setView("Tests.fxml", event);
            case "submissionsButton" -> setView("Submissions.fxml", event);
            default -> setView("Lounge.fxml", event);
        }

    }

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

}
