// File: client/admin/controller/ReservationApprovalController.java
package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.ApprovalReservation;
import server.utility.Terminal;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationApprovalController {

    private final ReservationApprovalView view;
    private final ReservationApprovalModel model;
    private final ObservableList<ApprovalReservation> allReservations = FXCollections.observableArrayList();
    private ObservableList<ApprovalReservation> reservationData = FXCollections.observableArrayList();

    public ReservationApprovalController(ReservationApprovalView view) {
        this.view = view;
        this.model = new ReservationApprovalModel();
    }

    public void loadReservationData() {
        reservationData = model.loadReservationData();
        view.setReservationData(reservationData);
    }

    public void saveChanges() {
        model.saveReservationData(reservationData); // Save only the current table data
        JOptionPane.showMessageDialog(null, "Changes have been successfully saved!",
                "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    public void searchTerminals(String searchText) {
        System.out.println("[DEBUG] Searching for terminals with keyword: " + searchText);

        if (searchText == null || searchText.trim().isEmpty()) {
            view.setReservationData(reservationData);
            System.out.println("[DEBUG] Search text is empty. Resetting to full terminal list.");
            return;
        }

        String lowerCaseQuery = searchText.toLowerCase();
        ObservableList<ApprovalReservation> filteredList = reservationData.stream()
                .filter(reservation -> reservation.getReservationId().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getUserId().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getTerminalId().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getRoomNumber().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getReservationDate().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getStartTime().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getEndTime().toLowerCase().contains(lowerCaseQuery) ||
                        reservation.getStatus().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        view.setReservationData(filteredList);
        System.out.println("[DEBUG] Search completed. Matching results: " + filteredList.size());
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

}
