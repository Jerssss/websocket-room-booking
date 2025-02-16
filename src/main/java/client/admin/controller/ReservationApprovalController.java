package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import server.utility.ApprovalReservation;
import java.util.List;

public class ReservationApprovalController {

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
    private final ObservableList<ApprovalReservation> reservations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Link table columns to ApprovalReservation properties
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        terminalNoColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load initial reservations
        loadReservations();
    }

    public void loadReservations() {
        List<ApprovalReservation> reservationList = model.loadReservations();
        reservations.setAll(reservationList);
        approveResTableView.setItems(reservations);
    }

    public void searchReservations(String keyword) {
        if (keyword.isEmpty()) {
            loadReservations();
            return;
        }

        List<ApprovalReservation> reservationList = model.loadReservations();
        List<ApprovalReservation> filteredList = reservationList.stream()
                .filter(res -> res.getUserId().contains(keyword) ||
                        res.getReservationId().contains(keyword) ||
                        res.getRoomId().contains(keyword) ||
                        res.getStatus().contains(keyword))
                .toList();

        reservations.setAll(filteredList);
        approveResTableView.setItems(reservations);
    }
}
