// client/student/model/ViewReservationModel.java
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
    private String sessionToken;

    public ViewReservationModel(String sessionToken) {
        this.sessionToken = sessionToken;
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public List<Reservation> fetchReservations() {
        return parseXML();
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }

    // Remove static modifier to use instance variable sessionToken
    private List<Reservation> parseXML() {
        return ViewReservationProcessor.loadReservationFromXML(sessionToken);
    }
}