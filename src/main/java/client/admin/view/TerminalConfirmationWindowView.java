package client.admin.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TerminalConfirmationWindowView {

    @FXML
    private Label descLabel;
    @FXML
    private Label terminalNoLabel;
    @FXML
    private Label roomNoLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    //button event handlers
    public void setActionConfirmButton(EventHandler<ActionEvent> event) {
        this.confirmButton.setOnAction(event);
    }
    public void setActionCancelButton(EventHandler<ActionEvent> event) {
        this.cancelButton.setOnAction(event);
    }

    //getters
    public Label getDescLabel() {
        return descLabel;
    }

    public Label getTerminalNoLabel() {
        return terminalNoLabel;
    }

    public Label getRoomNoLabel() {
        return roomNoLabel;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }


    //setters
    public void setDescLabel(Label descLabel) {
        this.descLabel = descLabel;
    }

    public void setTerminalNoLabel(Label terminalNoLabel) {
        this.terminalNoLabel = terminalNoLabel;
    }

    public void setRoomNoLabel(Label roomNoLabel) {
        this.roomNoLabel = roomNoLabel;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public void setConfirmButton(Button confirmButton) {
        this.confirmButton = confirmButton;
    }
}
