package client.student.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;

import java.io.IOException;

public class StudentMainMenuModel {
    private ServerConnection serverConnection;

    public StudentMainMenuModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
