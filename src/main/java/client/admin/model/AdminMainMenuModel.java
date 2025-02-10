package client.admin.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminMainMenuModel {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public AdminMainMenuModel() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
