package client.view;

import client.controller.landingpage.LandingPageController; // Ensure this import is included
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientView {

    private FXMLLoader fxmlLoader;
    private final Stage stage;

    // Constructor
    public ClientView(Stage stage) {
        this.stage = stage;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public LandingPageController getLandingPageController() {
        return fxmlLoader.getController(); // Method to get the controller
    }

    public void runGUI() {
        try {
            System.out.println("Loading client's interface...");
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/landing_page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Equipment Reservation App");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading FXML: " + e.getMessage(), e);
        }
    }
}
