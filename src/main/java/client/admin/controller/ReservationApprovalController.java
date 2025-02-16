// File: client/admin/controller/ReservationApprovalController.java
package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import server.utility.ApprovalReservation;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationApprovalController {

    private ReservationApprovalView view;
    private ReservationApprovalModel model;
    private ObservableList<ApprovalReservation> allReservations = FXCollections.observableArrayList();

    public ReservationApprovalController(ReservationApprovalModel reservationApprovalModel, ReservationApprovalView view) {
        this.view = view;
        this.model = reservationApprovalModel;

        loadReservations();         // Load all reservations initially
        setupSearchFunctionality(); // Set up search feature
        setupRefreshFunctionality();// Set up refresh feature
    }

    private void loadReservations() {
        List<ApprovalReservation> reservations = model.fetchAllApprovalReservations();
        System.out.println("loadReservations() called");
        allReservations.setAll(reservations);
        view.updateTable(allReservations);
    }

    private void setupSearchFunctionality() {
        view.setActionSearchButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchQuery = view.getSearchStudResTextField().getText().trim();
                filterReservations(searchQuery);
            }
        });
    }

    private void setupRefreshFunctionality() {
        view.setActionRefreshButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadReservations();
            }
        });
    }

    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.updateTable(allReservations);
            return;
        }

        List<ApprovalReservation> filteredList = allReservations.stream()
                .filter(reservation -> reservation.getRoomNumber().equalsIgnoreCase(searchQuery))
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }
}
