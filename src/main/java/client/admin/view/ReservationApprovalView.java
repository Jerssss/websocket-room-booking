// File: client/admin/view/ReservationApprovalView.java
package client.admin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.utility.ApprovalReservation;

public class ReservationApprovalView {

    @FXML
    private Button searchButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    private TextField searchStudResTextField;

    @FXML
    private TableView<ApprovalReservation> approveResTableView;

    @FXML
    private TableColumn<ApprovalReservation, String> reservationIdColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> userIdColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> terminalNoColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> roomNumberColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> dateColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> startTimeColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> endTimeColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> statusColumn;

    private ObservableList<ApprovalReservation> reservationData = FXCollections.observableArrayList();

    public TableView<ApprovalReservation> getApproveResTableView() {
        return approveResTableView;
    }

    // Button event setters
    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }

    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
    }
    public void setActionSaveChangesButton(EventHandler<ActionEvent> event) {
        saveChangesButton.setOnAction(event);
    }


    @FXML
    public void initialize() {
        // Initialize table columns
        reservationIdColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        terminalNoColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        approveResTableView.setItems(reservationData);
    }

    // Method to update the table with new data
    public void updateTable(ObservableList<ApprovalReservation> reservations) {
        reservationData.setAll(reservations);
        approveResTableView.setItems(reservationData);
    }

    // Getter for search field
    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }
}
