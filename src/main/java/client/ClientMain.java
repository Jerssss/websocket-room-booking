package client;

import client.landingpage.LandingPageController;
import client.utility.ServerConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        // Show the GUI first
        Platform.runLater(() -> {
            stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
            ClientView view = new ClientView(stage);
            view.runInterface();
            new ClientController(view);
        });

        // Start the server connection in a background thread
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

    // Inner class representing the ClientView
    private class ClientView {
        private FXMLLoader fxmlLoader;
        private final Stage stage;

        public ClientView(Stage stage) {
            this.stage = stage;
        }

        public void runInterface() {
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/landing_page.fxml")); //calls the javafx scene
                Scene scene = new Scene(fxmlLoader.load()); //reads from the file, takes in the nodes, stores it in Scene scene
                stage.setTitle("Lendify");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public FXMLLoader getFxmlLoader() {
            return fxmlLoader;
        }

        public Stage getStage() {
            return stage;
        }
    }

    // Inner class representing the ClientController
    private class ClientController {
        public ClientController(ClientView view) {
            System.out.println("Loading client's landing page controller...");
            new LandingPageController(view.getFxmlLoader().getController());
        }
    }

    // Inner class representing the ClientModel
    private class ClientModel {
        // Model logic can be added here if needed
    }
}