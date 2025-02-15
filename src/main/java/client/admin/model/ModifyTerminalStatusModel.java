package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;

import java.io.IOException;

public class ModifyTerminalStatusModel {
    private ServerConnection serverConnection;

    public ModifyTerminalStatusModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

