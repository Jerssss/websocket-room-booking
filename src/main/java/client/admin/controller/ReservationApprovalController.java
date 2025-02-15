package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import server.utility.StudentReservation;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationApprovalController {

    private final ReservationApprovalView view;
    private final ReservationApprovalModel model;
    private final ObservableList<StudentReservation> allReservations = FXCollections.observableArrayList();

    public ReservationApprovalController(ReservationApprovalView view) {
        this.view = view;
        this.model = new ReservationApprovalModel();

        loadReservations();  // Load all reservations
        setupSearchFunctionality();

        this.view.setActionRefreshButton(event -> loadReservations());
    }

    /** Load all reservations */
    private void loadReservations() {
        List<StudentReservation> reservations = model.fetchApprovalReservations();
        allReservations.setAll(reservations);
        view.displayApprovalReservations(allReservations);
    }

    /** Set up search button functionality */
    private void setupSearchFunctionality() {
        view.setActionSearchButton(event -> {
            String searchQuery = view.getSearchStudResTextField().getText().trim();
            filterReservations(searchQuery);
        });
    }

    /** Filter reservations based on search query */
    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.displayApprovalReservations(allReservations); // Show all if empty
            return;
        }

        List<StudentReservation> filteredList = allReservations.stream()
                .filter(reservation -> reservation.getTerminalRoom() != null &&
                        reservation.getTerminalRoom().equalsIgnoreCase(searchQuery))
                .collect(Collectors.toList());

        view.displayApprovalReservations(filteredList);
    }
}
