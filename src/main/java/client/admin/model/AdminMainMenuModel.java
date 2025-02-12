package client.admin.model;

import client.utility.ServerConnection;
import java.io.IOException;

public class AdminMainMenuModel {
    private ServerConnection serverConnection;

    public AdminMainMenuModel() {
        try {
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
