package client;

import client.utility.ServerConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import java.io.IOException;

public class ClientMain extends Application {
    private ServerConnection serverConnection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        if (!requestConnection()) {
            showDisconnectedDialog();
            return;
        }

        stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
        ClientView view = new ClientView(stage);
        view.runInterface();

        new ClientController(view);
    }

    public boolean requestConnection() {
        Thread thread = new Thread(() -> {
            while (true) { // Keep retrying until connected
                try {
                    serverConnection = new ServerConnection();
                    System.out.println("Connected to the server.");
                    System.out.println(serverConnection.readMessage()); // Read welcome message

                    // Listen for messages from the server
                    String serverResponse;
                    while ((serverResponse = serverConnection.readMessage()) != null) {
                        System.out.println(serverResponse);
                        if ("Goodbye!".equalsIgnoreCase(serverResponse)) {
                            break;
                        }
                    }

                    // If we reach here, the server has closed the connection
                    showDisconnectedDialog();
                    break; // Exit the loop after showing the dialog

                } catch (IOException e) {
                    showDisconnectedDialog();
                    try {
                        Thread.sleep(3000); // Wait before retrying
                    } catch (InterruptedException ignored) {}
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return serverConnection != null;
    }

    private void showDisconnectedDialog() {
        Platform.runLater(() ->
                JOptionPane.showMessageDialog(null,
                        "Disconnected from the server.",
                        "Connection Lost",
                        JOptionPane.ERROR_MESSAGE)
        );
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() ->
                JOptionPane.showMessageDialog(null,
                        message,
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE)
        );
    }
}
