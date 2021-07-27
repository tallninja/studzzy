package application.controllers.views;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public ActionButtonTableCell(String label, Function<S, S> callBackFunction) {
        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent event) -> {
            callBackFunction.apply(getCurrentItem()); // apply the callback function to the current row
        });
        this.actionButton.getStyleClass().add("cta-button");
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    // gets the current object on the table row
    public S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function<S, S> function) {
        return param -> new ActionButtonTableCell<>(label, function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }

}
