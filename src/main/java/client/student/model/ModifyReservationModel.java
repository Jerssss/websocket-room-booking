package client.student.model;

import client.utility.ServerConnection;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import client.utility.ServerConnectionManager;
import client.utility.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.student.ModifyReservationProcessor;
import server.utility.Reservation;

public class ModifyReservationModel {
    private ServerConnection serverConnection;
    private String sessionToken;

    public ModifyReservationModel(String sessionToken) {
        this.sessionToken = sessionToken;
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Reservation> loadReservationData() {
        List<Reservation> allReservations = ModifyReservationProcessor.parseXML();
        String currentUserId = SessionManager.getUserId(sessionToken);

        List<Reservation> userReservations = allReservations.stream()
                .filter(res -> res.getUserId().equals(currentUserId))
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(userReservations);
    }

    public ObservableList<Reservation> searchReservations(String searchText) {
        List<Reservation> allReservations = ModifyReservationProcessor.parseXML();
        String currentUserId = SessionManager.getUserId(sessionToken);

        return allReservations.stream()
                .filter(res -> res.getUserId().equals(currentUserId))
                .filter(res -> matchesSearch(res, searchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public void saveReservationData(ObservableList<Reservation> reservationData) {
        if (serverConnection != null) {
            ModifyReservationProcessor.saveToXML(reservationData);
        }
    }

    private boolean matchesSearch(Reservation res, String searchText) {
        String lowerSearch = searchText.toLowerCase();
        return res.getReservationId().toLowerCase().contains(lowerSearch) ||
                res.getRoomNumber().toLowerCase().contains(lowerSearch) ||
                res.getTerminalNumber().toLowerCase().contains(lowerSearch);
    }
}