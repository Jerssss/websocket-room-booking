package client.admin.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.admin.controller.AddNewTerminalController;

import java.io.IOException;

public class AddNewTerminalView {

    @FXML
    private Button saveChangesButton;
    @FXML
    private Button cancelButton; // Example: a Cancel button to go back to the Admin Main Menu
    @FXML
    private BorderPane rootPane;
    @FXML
    private Button openAddTerminalWindowButton;

    // Load a new view inside the Add New Terminal pane
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void openAddTerminalWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_terminal_window.fxml"));
            Parent root = loader.load();

            // Open in a new stage
            Stage stage = new Stage();
            stage.setTitle("Add New Terminal");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void initialize() {
        if (openAddTerminalWindowButton != null) {
            openAddTerminalWindowButton.setOnAction(event -> openAddTerminalWindow(event));
        }
    }
}

