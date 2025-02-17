package client.student.model;

import client.utility.ServerConnection;

import java.io.IOException;
import java.util.List;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import server.student.ViewReservationProcessor;
import server.utility.Reservation;
import javax.swing.*;

public class ViewReservationModel {
    private ServerConnection serverConnection;

    public ViewReservationModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    // Fetch  reservations from XML using the processor
    public List<Reservation> fetchReservations() {
        return parseXML();
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
    public static List<Reservation> parseXML() {
        return ViewReservationProcessor.loadStudentReservationsFromXML();
    }
}