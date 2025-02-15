package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;

import java.io.IOException;

public class AdminMainMenuModel {
    private ServerConnection serverConnection;

    public AdminMainMenuModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
