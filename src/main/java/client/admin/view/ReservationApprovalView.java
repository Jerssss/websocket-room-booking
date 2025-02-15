package client.admin.view;

import client.admin.controller.ReservationApprovalController;
import client.utility.TableUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.utility.StudentReservation;

public class ReservationApprovalView {

    @FXML private TextField searchStudResTextField;
    @FXML private Button searchButton;
    @FXML private TableView<StudentReservation> approveResTableView;
    @FXML private TableColumn<StudentReservation, String> resIDColumn;
    @FXML private TableColumn<StudentReservation, String> terminalColumn;
    @FXML private TableColumn<StudentReservation, String> roomNumberColumn;
    @FXML private TableColumn<StudentReservation, String> dateColumn;
    @FXML private TableColumn<StudentReservation, String> statusColumn;

    // Bind search button to the controller
    public void setSearchButtonAction(EventHandler<ActionEvent> handler) {
        searchButton.setOnAction(handler);
    }

    // Bind data to the TableView
    public void setTableData(ObservableList<StudentReservation> reservations) {
        approveResTableView.setItems(reservations);
    }

    // Get search field value
    public String getSearchKeyword() {
        return searchStudResTextField.getText().trim();
    }

    // Set up table columns
    public void initializeTable() {
        TableUtils.setupColumn(resIDColumn, "reservationId");
        TableUtils.setupColumn(terminalColumn, "terminalId");
        TableUtils.setupColumn(roomNumberColumn, "terminalRoom");
        TableUtils.setupColumn(dateColumn, "date");
        TableUtils.setupColumn(statusColumn, "terminalStatus");
    }
}
