package client.admin.model;

import client.utility.ServerConnection;
import java.io.IOException;

public class ModifyTerminalStatusModel {
    private ServerConnection serverConnection;

    public ModifyTerminalStatusModel() {
        try {
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

