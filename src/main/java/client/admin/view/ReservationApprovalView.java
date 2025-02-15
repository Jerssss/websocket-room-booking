package client.admin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import server.utility.ApprovalTerminal;
import server.utility.StudentReservation;

public class ReservationApprovalView {

    @FXML
    private TextField searchStudResTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<StudentReservation> approveResTableView;

    @FXML
    private TableColumn<StudentReservation, String> resIDColumn;

    @FXML
    private TableColumn<StudentReservation, String> userIDColumn;

    @FXML
    private TableColumn<StudentReservation, String> terminalColumn;

    @FXML
    private TableColumn<StudentReservation, String> roomNumberColumn;

    @FXML
    private TableColumn<StudentReservation, String> dateColumn;

    @FXML
    private TableColumn<StudentReservation, String> statusColumn;

    private final ObservableList<StudentReservation> reservationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns with property values
        resIDColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        terminalColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("terminalRoom"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("terminalStatus"));

        approveResTableView.setItems(reservationList);
    }

    /** Set Search Button Action */
    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }

    /** Set Refresh Button Action */
    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
    }

    /** Display Reservations */
    public void displayApprovalReservations(ObservableList<ApprovalTerminal> reservations) {
        reservationList.setAll((StudentReservation) reservations);
    }

    /** Get Search TextField */
    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }
}
