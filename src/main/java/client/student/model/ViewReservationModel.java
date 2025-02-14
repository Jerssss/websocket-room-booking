package client.student.model;

import client.utility.ServerConnection;

import java.io.IOException;

public class ViewReservationModel {
    private final ServerConnection serverConnection;

    public ViewReservationModel() throws IOException {
        serverConnection = new ServerConnection();
    }

    public String fetchAllReservations() {
        try {
            // Send the request to the server (e.g., "FETCH_RESERVATIONS")
            serverConnection.sendMessage("FETCH_RESERVATIONS");

            // Read the response from the server (plain string)
            String response = serverConnection.readMessage();

            return response; // Return the plain string

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        serverConnection.close();
    }
}
