package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        requestConnection();

        stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
        ClientView view = new ClientView(stage);
        view.runInterface();

        new ClientController(view);
    }

    public void requestConnection() {
        Thread thread = new Thread(() -> {
            while (true) { // Keep retrying until connected
                try {
                    socket = new Socket(SERVER_HOST, SERVER_PORT);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);

                    System.out.println("Connected to the server.");
                    System.out.println(reader.readLine()); // Read welcome message

                    // Listen for messages from the server
                    String serverResponse;
                    while ((serverResponse = reader.readLine()) != null) {
                        System.out.println(serverResponse);
                        if ("Goodbye!".equalsIgnoreCase(serverResponse)) {
                            break;
                        }
                    }

                    // If we reach here, the server has closed the connection
                    showDisconnectedDialog();
                    break; // Exit the loop after showing the dialog

                } catch (UnknownHostException e) {
                    showErrorDialog("Unknown host: " + SERVER_HOST);
                    break;
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
