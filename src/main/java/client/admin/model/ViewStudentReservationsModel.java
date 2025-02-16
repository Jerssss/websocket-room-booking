package client.admin.model;


import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import server.admin.ViewStudentReservationsProcessor;
import server.utility.StudentReservation;
import javax.swing.*;
import java.io.IOException;
import java.util.List;


public class ViewStudentReservationsModel {
    private ServerConnection serverConnection;

    public ViewStudentReservationsModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    // Fetch student reservations from XML using the processor
    public List<StudentReservation> fetchStudentReservations() {
        return parseXML();
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
    public static List<StudentReservation> parseXML() {
        return ViewStudentReservationsProcessor.loadStudentReservationsFromXML();
    }
}
