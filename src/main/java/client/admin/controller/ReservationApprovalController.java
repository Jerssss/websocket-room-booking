package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import server.utility.StudentReservation;

public class ReservationApprovalController {

    private final ReservationApprovalModel model;
    private final ReservationApprovalView view;

    public ReservationApprovalController(ReservationApprovalView view, ReservationApprovalModel model) {
        this.view = view;
        this.model = model;

        // Set up table and search action
        this.view.initializeTable();
        this.view.setSearchButtonAction(this::handleSearch);

        // Load all reservations on startup
        loadAllReservations();
    }

    // Load all reservations from model
    private void loadAllReservations() {
        ObservableList<StudentReservation> reservations = model.loadAllReservations();
        view.setTableData(reservations);
    }

    // Filter results based on search keyword
    private void handleSearch(ActionEvent event) {
        String keyword = view.getSearchKeyword().toLowerCase();
        ObservableList<StudentReservation> allReservations = model.loadAllReservations();
        ObservableList<StudentReservation> filtered = FXCollections.observableArrayList();

        for (StudentReservation res : allReservations) {
            if (res.getReservationId().toLowerCase().contains(keyword) ||
                    res.getTerminalId().toLowerCase().contains(keyword) ||
                    res.getTerminalRoom().toLowerCase().contains(keyword) ||
                    res.getDate().toLowerCase().contains(keyword) ||
                    res.getTerminalStatus().toLowerCase().contains(keyword)) {
                filtered.add(res);
            }
        }
        view.setTableData(filtered);
    }
}
