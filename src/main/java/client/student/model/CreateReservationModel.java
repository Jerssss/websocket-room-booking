package client.student.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import server.student.CreateReservationProcessor;

import javax.swing.*;

import java.io.IOException;

public class CreateReservationModel {
    private ServerConnection serverConnection;
    private final CreateReservationProcessor processor;


    public CreateReservationModel() {
        this.processor = new CreateReservationProcessor();
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
