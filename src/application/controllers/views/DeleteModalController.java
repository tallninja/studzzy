package application.controllers.views;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;


public class DeleteModalController {

    public static boolean isDelete;

    public void abortDelete(ActionEvent event) {
        isDelete = false;
        Stage stage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        stage.close();
    }

    public void confirmDelete(ActionEvent event) {
        isDelete = true;
        Stage stage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        stage.close();
    }

}
