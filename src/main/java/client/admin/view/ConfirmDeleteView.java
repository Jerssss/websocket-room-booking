package client.admin.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmDeleteView {

    @FXML
    private Label roomNoLabel;
    @FXML
    private Label terminalNoLabel;
    @FXML
    private Label terminalOSLabel;
    @FXML
    private Label terminalStatusLabel;
    @FXML
    private Label warningLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    //button action setters
    public void setActionCancelButton(EventHandler<ActionEvent> event) {
        this.cancelButton.setOnAction(event);
    }
    public void setActionConfirmButton(EventHandler<ActionEvent> event) {
        this.confirmButton.setOnAction(event);
    }

    //getters
    public Label getRoomNoLabel() {
        return roomNoLabel;
    }

    public Label getTerminalNoLabel() {
        return terminalNoLabel;
    }

    public Label getTerminalOSLabel() {
        return terminalOSLabel;
    }

    public Label getTerminalStatusLabel() {
        return terminalStatusLabel;
    }

    public Label getWarningLabel() {
        return warningLabel;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    //setters
    public void setRoomNoLabel(Label roomNoLabel) {
        this.roomNoLabel = roomNoLabel;
    }

    public void setTerminalNoLabel(Label terminalNoLabel) {
        this.terminalNoLabel = terminalNoLabel;
    }

    public void setTerminalOSLabel(Label terminalOSLabel) {
        this.terminalOSLabel = terminalOSLabel;
    }

    public void setTerminalStatusLabel(Label terminalStatusLabel) {
        this.terminalStatusLabel = terminalStatusLabel;
    }

    public void setWarningLabel(Label warningLabel) {
        this.warningLabel = warningLabel;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public void setConfirmButton(Button confirmButton) {
        this.confirmButton = confirmButton;
    }
}
