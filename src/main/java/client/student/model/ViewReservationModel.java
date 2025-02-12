package client.student.model;

import client.utility.ServerConnection;
import java.io.IOException;

public class ViewReservationModel {
    private ServerConnection serverConnection;

    public ViewReservationModel() {
        try {
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fetchAllReservations() {
        if (serverConnection == null) return null;

        try {
            serverConnection.sendMessage("<FetchAllReservations />");
            return serverConnection.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
