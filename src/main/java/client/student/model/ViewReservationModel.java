package client.student.model;

import client.utility.ServerConnection;
import server.student.ViewReservationProcessor; // Import the processor class

import java.io.IOException;
import java.util.Map;

public class ViewReservationModel {
    private final String xmlFilePath = "src/main/java/server/util/reserved.xml"; // project path
    private ServerConnection serverConnection;
    private final ViewReservationProcessor processor; // Add processor instance

    public ViewReservationModel() {
        try {
            // Establish a connection to the server
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            System.err.println("Failed to connect to the server: " + e.getMessage());
        }
        this.processor = new ViewReservationProcessor(); // Initialize the processor
    }

    public Map<String, Map<String, String>> fetchAllReservations() {
        return processor.fetchAllReservations(); // Delegate to the processor
    }
}