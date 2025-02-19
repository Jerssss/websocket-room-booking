package client.student.controller;

import client.student.model.ModifyReservationModel;
import client.student.view.ModifyReservationView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.Reservation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyReservationController {
    private final ModifyReservationView view;
    private final ModifyReservationModel model;
    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    public ModifyReservationController(ModifyReservationView view, String sessionToken) {
        this.view = view;
        this.model = new ModifyReservationModel(sessionToken);
    }

    // Only update in-memory data, don't save to XML
    public void updateReservation(Reservation updatedReservation) {
        for (int i = 0; i < reservationData.size(); i++) {
            Reservation reservation = reservationData.get(i);
            if (reservation.getReservationId().equals(updatedReservation.getReservationId())) {
                reservationData.set(i, updatedReservation);
                view.setReservationData(reservationData); // Update view only
                break;
            }
        }
    }

    // Explicit save command
    public void saveChanges() {
        model.saveReservationData(reservationData);
        JOptionPane.showMessageDialog(null, "Changes saved!");
    }

    public void loadReservationData() {
        reservationData = model.loadReservationData();
        view.setReservationData(reservationData);
    }

    public List<Reservation> getCurrentReservations() {
        return new ArrayList<>(reservationData);
    }

    public void removeReservation(Reservation reservation) {
        reservationData.remove(reservation);
        view.setReservationData(reservationData);
    }

    public void searchReservations(String searchText) {
        ObservableList<Reservation> filteredList = reservationData.stream()
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
}