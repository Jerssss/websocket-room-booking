package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ModifyReservationWindowView {

    @FXML
    private Label reservationIDLabel;
    @FXML
    private Label idToModifyLabel;
    @FXML
    private Label reservedDateLabel;
    @FXML
    private Label reservedTimeLabel;
    @FXML
    private Label reservedRoomNoLabel;
    @FXML
    private Label reservedTerminalNoLabel;

    @FXML
    private TextField newDateTextField;
    @FXML
    private TextField newTimeTextField;
    @FXML
    private TextField newRoomTextField;
    @FXML
    private TextField newTerminalTextField;

    @FXML
    private Label invalidTerminalPromptLabel;
    @FXML
    private Label invalidRoomPromptLabel;
    @FXML
    private Label invalidTimePromptLabel;
    @FXML
    private Label invalidDatePromptLabel;

    @FXML
    private Button sendRequestButton;
    @FXML
    private Button deleteReservationButton;


    //action calls for buttons
    public void setActionSendRequestButton(EventHandler<ActionEvent> event) {
        sendRequestButton.setOnAction(event);
    }

    public void setActionDeleteReservationButton(EventHandler<ActionEvent> event) {
        deleteReservationButton.setOnAction(event);
    }

    //getters
    public Label getReservationIDLabel() {
        return reservationIDLabel;
    }

    public Label getIdToModifyLabel() {
        return idToModifyLabel;
    }

    public Label getReservedDateLabel() {
        return reservedDateLabel;
    }

    public Label getReservedTimeLabel() {
        return reservedTimeLabel;
    }

    public Label getReservedRoomNoLabel() {
        return reservedRoomNoLabel;
    }

    public Label getReservedTerminalNoLabel() {
        return reservedTerminalNoLabel;
    }

    public TextField getNewDateTextField() {
        return newDateTextField;
    }

    public TextField getNewTimeTextField() {
        return newTimeTextField;
    }

    public TextField getNewRoomTextField() {
        return newRoomTextField;
    }

    public TextField getNewTerminalTextField() {
        return newTerminalTextField;
    }


    //setters
    public void setReservationIDLabel(Label reservationIDLabel) {
        this.reservationIDLabel = reservationIDLabel;
    }

    public void setIdToModifyLabel(Label idToModifyLabel) {
        this.idToModifyLabel = idToModifyLabel;
    }

     public void setReservedDateLabel(Label reservedDateLabel) {
        this.reservedDateLabel = reservedDateLabel;
    }

    public void setReservedTimeLabel(Label reservedTimeLabel) {
        this.reservedTimeLabel = reservedTimeLabel;
    }

    public void setReservedRoomNoLabel(Label reservedRoomNoLabel) {
        this.reservedRoomNoLabel = reservedRoomNoLabel;
    }

    public void setReservedTerminalNoLabel(Label reservedTerminalNoLabel) {
        this.reservedTerminalNoLabel = reservedTerminalNoLabel;
    }

    //invalid prompt label setter calls
    public void setInvalidDatePromptLabel(Label invalidDatePromptLabel) {
        this.invalidDatePromptLabel = invalidDatePromptLabel;
    }

    public void setInvalidTimePromptLabel(Label invalidTimePromptLabel) {
        this.invalidTimePromptLabel = invalidTimePromptLabel;
    }

    public void setInvalidRoomPromptLabel(Label invalidRoomPromptLabel) {
        this.invalidRoomPromptLabel = invalidRoomPromptLabel;
    }

    public void setInvalidTerminalPromptLabel(Label invalidTerminalPromptLabel) {
        this.invalidTerminalPromptLabel = invalidTerminalPromptLabel;
    }
}
