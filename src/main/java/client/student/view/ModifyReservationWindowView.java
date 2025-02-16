package client.student.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import server.student.ModifyReservationProcessor;
import server.utility.Reservation;

import java.util.List;

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

    @FXML
    private TableView<Reservation> modResTableView;

    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;

    @FXML
    private TableColumn<Reservation, String> userIDColumn;

    @FXML
    private TableColumn<Reservation, String> terminalIDColumn;

    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;

    @FXML
    private TableColumn<Reservation, String> startTimeColumn;

    @FXML
    private TableColumn<Reservation, String> endTimeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    public TableView<Reservation> getStudResTableView() {
        return modResTableView;  // Ensure studResTableView is properly initialized
    }


    //action calls for buttons
    public void setActionSendRequestButton(EventHandler<ActionEvent> event) {
        sendRequestButton.setOnAction(event);
    }

    public void setActionDeleteReservationButton(EventHandler<ActionEvent> event) {
        deleteReservationButton.setOnAction(event);
    }


    public void initialize() {
        // Initialize columns and bind properties
        reservationIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        userIDColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        terminalIDColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().reservationStatusProperty());


        loadDataFromXML("src/main/java/server/util/reservationapproval.xml");

        modResTableView.setItems(reservationData);
    }
    private void loadDataFromXML(String filePath) {
        List<Reservation> reservations = ModifyReservationProcessor.parseXML(filePath);
        if (reservations != null) {
            reservationData.addAll(reservations);
        }
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
