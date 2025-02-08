package client;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class ClientView {

    private FXMLLoader fxmlLoader;
    private final Stage stage;

    public ClientView(Stage stage) {
        this.stage = stage;
    }

    public void runInterface() {
        try{
            System.out.println("Loading initial scene...");
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/landing_page.fxml")); //calls the javafx scene
            Scene scene = new Scene (fxmlLoader.load()); //reads from the file, takes in the nodes, stores it in Scene scene
            stage.setTitle("Lendify");
            stage.setScene(scene);
            stage.show();
        }catch (IOException ioe) {
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
