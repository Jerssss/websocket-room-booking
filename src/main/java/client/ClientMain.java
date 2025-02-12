package client;

import client.utility.ServerConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMain extends Application {
    private ServerConnection serverConnection;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean isConnected = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // ✅ Show the GUI first
        Platform.runLater(() -> {
            stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
            ClientView view = new ClientView(stage);
            view.runInterface();
            new ClientController(view);
        });

        // ✅ Start the server connection in a background thread
        executor.execute(this::requestConnection);
    }

    public void requestConnection() {
        try {
            serverConnection = new ServerConnection();
            isConnected = true;
            System.out.println("Connected to the server.");
            listenForServerMessages();
        } catch (IOException e) {
            isConnected = false;
            showDisconnectedDialog();
        }
    }

    private void listenForServerMessages() {
        new Thread(() -> {
            try {
                while (isConnected) {
                    String serverResponse = serverConnection.readMessage();
                    if (serverResponse == null) {
                        System.out.println("Server connection lost. Retrying...");
                        isConnected = false;
                        executor.execute(this::requestConnection);
                        return;
                    }
                    System.out.println("Server: " + serverResponse);
                    if ("Goodbye!".equalsIgnoreCase(serverResponse)) {
                        isConnected = false;
                        showDisconnectedDialog();
                        return;
                    }
                }
            } catch (IOException e) {
                isConnected = false;
                showDisconnectedDialog();
            }
        }).start();
    }

    private void showDisconnectedDialog() {
        Platform.runLater(() -> {
            JOptionPane.showMessageDialog(null,
                    "Disconnected from the server.",
                    "Connection Lost",
                    JOptionPane.ERROR_MESSAGE);
        });
    }
}
