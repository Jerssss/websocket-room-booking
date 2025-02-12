package client.admin.model;

import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

import client.admin.view.AddNewTerminalView;
import javafx.application.Platform;

public class AddNewTerminalModel {

    private AddNewTerminalView view;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;


    public AddNewTerminalModel() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}