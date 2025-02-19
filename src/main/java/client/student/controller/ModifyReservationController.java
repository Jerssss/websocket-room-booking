package client.student.controller;

import client.student.model.ModifyReservationModel;
import client.student.view.ModifyReservationView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.Reservation;

import javax.swing.*;
import java.util.stream.Collectors;

public class ModifyReservationController {
    private final ModifyReservationView view;
    private final ModifyReservationModel model;
    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    public ModifyReservationController(ModifyReservationView view, String sessionToken) {
        this.view = view;
        this.model = new ModifyReservationModel(sessionToken); // Pass sessionToken to model
    }

    public void updateReservation(Reservation updatedReservation) {
        // Find the reservation by ID and update it
        for (int i = 0; i < reservationData.size(); i++) {
            Reservation reservation = reservationData.get(i);
            if (reservation.getReservationId().equals(updatedReservation.getReservationId())) {
                reservation.setStatus(updatedReservation.getStatus()); // Update status to Pending
                reservation.setDate(updatedReservation.getDate());
                reservation.setStartTime(updatedReservation.getStartTime());
                reservation.setEndTime(updatedReservation.getEndTime());

                // Refresh the view
                model.saveReservationData(reservationData);
                view.setReservationData(reservationData);
                break;
            }
        }
    }

    public void loadReservationData() {
        reservationData = model.loadReservationData();
        view.setReservationData(reservationData); // Update the view
        System.out.println("Data loaded. Reservations: " + reservationData.size());
    }

    public void removeReservation(Reservation reservation) {
        reservationData.remove(reservation);
        view.setReservationData(reservationData);
        System.out.println("Reservation removed.");
    }

    public void searchReservations(String searchText) {
        ObservableList<Reservation> filteredList = reservationData.stream() // Use reservationData instead of fetching all
                .filter(res -> matchesSearch(res, searchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        view.setReservationData(filteredList);
    }

    private boolean matchesSearch(Reservation res, String searchText) {
        String lowerSearch = searchText.toLowerCase();
        return res.getReservationId().toLowerCase().contains(lowerSearch) ||
                res.getRoomNumber().toLowerCase().contains(lowerSearch) ||
                res.getTerminalNumber().toLowerCase().contains(lowerSearch) ||
                res.getDate().toLowerCase().contains(lowerSearch) ||
                res.getStartTime().toLowerCase().contains(lowerSearch) ||
                res.getEndTime().toLowerCase().contains(lowerSearch) ||
                res.getStatus().toLowerCase().contains(lowerSearch);
    }

    public void saveChanges() {
        model.saveReservationData(reservationData);
        JOptionPane.showMessageDialog(null, "Changes saved!");
    }
}
