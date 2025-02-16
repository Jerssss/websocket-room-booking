// File: client/admin/view/ReservationApprovalView.java
package client.admin.view;

import client.admin.model.ReservationApprovalModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.utility.ApprovalReservation;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ReservationApprovalView {

    @FXML
    private VBox centerPane;

    @FXML
    private Label approveResLabel;

    @FXML
    private TextField searchStudResTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button refreshButton;

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

    private final ReservationApprovalModel model = new ReservationApprovalModel();

    @FXML
    public void initialize() {
        // Initialize Table Columns with Property Names
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        terminalNoColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Event Listeners
        searchButton.setOnAction(event -> searchReservations());
        refreshButton.setOnAction(event -> refreshTable());

        // Load Data into Table on Start
        loadReservations();
    }

    // Load Reservations from Model
    private void loadReservations() {
        List<ApprovalReservation> reservations = model.getReservations();
        ObservableList<ApprovalReservation> reservationList = FXCollections.observableArrayList(reservations);
        approveResTableView.setItems(reservationList);
    }

    // Search Reservations (Filter by ID or User)
    private void searchReservations() {
        String keyword = searchStudResTextField.getText().trim();
        if (!keyword.isEmpty()) {
            List<ApprovalReservation> filteredReservations = model.getReservations().stream()
                    .filter(reservation -> reservation.getReservationId().contains(keyword) ||
                            reservation.getUserId().contains(keyword))
                    .toList();
            approveResTableView.setItems(FXCollections.observableArrayList(filteredReservations));
        }
    }

    // Refresh Table
    private void refreshTable() {
        searchStudResTextField.clear();
        loadReservations();
    }
}
