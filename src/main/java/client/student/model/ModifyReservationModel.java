package client.student.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import client.utility.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.student.ModifyReservationProcessor;
import server.utility.Reservation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyReservationModel {
    private ServerConnection serverConnection;
    private String sessionToken;
    private List<Reservation> allReservations;

    public ModifyReservationModel(String sessionToken) {
        this.sessionToken = sessionToken;
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Reservation> loadReservationData() {
        allReservations = ModifyReservationProcessor.parseXML();
        String currentUserId = SessionManager.getUserId(sessionToken);

        return FXCollections.observableArrayList(
                allReservations.stream()
                        .filter(res -> res.getUserId().equals(currentUserId))
                        .filter(res -> "Pending".equals(res.getStatus())) // Only load Pending reservations
                        .collect(Collectors.toList())
        );
    }

    public List<Reservation> getAllReservations() {
        return ModifyReservationProcessor.parseXML(); // Load all reservations
    }

    public void saveReservationData(ObservableList<Reservation> userReservations) {
        String currentUserId = SessionManager.getUserId(sessionToken);

        // Get fresh data from XML
        List<Reservation> currentReservations = ModifyReservationProcessor.parseXML();

        // Remove old user reservations
        currentReservations.removeIf(res -> res.getUserId().equals(currentUserId));

        // Add updated reservations
        currentReservations.addAll(userReservations);

        // Save merged list
        ModifyReservationProcessor.saveToXML(currentReservations);

        // Update cache
        allReservations = currentReservations;
    }
}