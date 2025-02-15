package client.utility;

import java.io.IOException;

public class ServerConnectionManager {
    public static ServerConnection serverConnection;

    // Private constructor to prevent direct instantiation
    private ServerConnectionManager() { }

    public static ServerConnection getConnection() throws IOException {
        if (serverConnection == null) {
            serverConnection = new ServerConnection();
        }
        return serverConnection;
    }

    public static void closeConnection() {
        if (serverConnection != null) {
            serverConnection.close();
            serverConnection = null;
        }
    }
}
