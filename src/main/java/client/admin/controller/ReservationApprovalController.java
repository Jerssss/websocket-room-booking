// File: client/admin/controller/ReservationApprovalController.java
package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import server.utility.ApprovalReservation;

import javax.swing.*;
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
        setupSaveChangesFunctionality(); // Set up save changes feature
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

    private void setupSaveChangesFunctionality() {
        view.setActionSaveChangesButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveChanges();
            }
        });
    }

    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.updateTable(allReservations);
            return;
        }

        // Convert search query to lowercase for case-insensitive matching
        String lowerCaseQuery = searchQuery.toLowerCase();

        // Filter reservations based on matching any column (reservationId, userId, terminalNo, etc.)
        List<ApprovalReservation> filteredList = allReservations.stream()
                .filter(reservation -> reservation.getReservationId().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getUserId().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getTerminalId().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getRoomNumber().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getReservationDate().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getStartTime().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getEndTime().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getStatus().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        // Update the table with filtered list
        view.updateTable(FXCollections.observableArrayList(filteredList));
    }


    private void saveChanges() {
        // Iterate over all reservations and update their status in the model
        for (ApprovalReservation reservation : allReservations) {
            String updatedStatus = reservation.getStatus();
            model.updateReservationStatus(reservation.getReservationId(), updatedStatus);
        }

        // After updating the status, save the updated reservations back to the XML
        model.saveUpdatedReservationsToXML(allReservations);

        // Show confirmation message
        JOptionPane.showMessageDialog(null, "Changes have been successfully saved!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

}
