package client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    //method to run the landing page
    public void runGUI() {
        try {
            System.out.println("Loading client's interface...");
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/landing_page.fxml"));

            //initial scene setup for everything
            Parent root = fxmlLoader.load(); //store landing page elements into Parent object root
            Scene scene = new Scene(root);

            stage.setTitle("Equipment Reservation App");
            stage.setScene(scene);
            stage.show();//display stage/window
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading FXML: " + e.getMessage(), e);
        }
    }
}
