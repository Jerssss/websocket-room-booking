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

    public String fetchRoomDetails(String roomId) {
        if (serverConnection == null) return null;

        try {
            String request = String.format("<FetchRoomDetails><RoomID>%s</RoomID></FetchRoomDetails>", roomId);
            serverConnection.sendMessage(request);
            return serverConnection.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String fetchTerminalDetails(String roomId) {
        if (serverConnection == null) return null;

        try {
            String request = String.format("<FetchTerminalDetails><RoomID>%s</RoomID></FetchTerminalDetails>", roomId);
            serverConnection.sendMessage(request);
            return serverConnection.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
