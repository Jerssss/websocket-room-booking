package client.student.model;

import client.utility.ServerConnection;
import java.io.IOException;

public class RoomItemCardModel {
    private ServerConnection serverConnection;

    public RoomItemCardModel() {
        try {
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fetchAllTerminalDetails() {
        if (serverConnection == null) return null;

        try {
            serverConnection.sendMessage("<FetchAllTerminals />");
            return serverConnection.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
