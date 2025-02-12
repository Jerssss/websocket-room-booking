package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import server.admin.AddNewTerminalProcessor; // Server-side processor

import java.io.IOException;

public class AddNewTerminalController {

    @FXML
    private TextField terminalNoTextField;

    @FXML
    private TextField roomTextField;

    @FXML
    private TextField osTypeTextField;

    @FXML
    private TextField statusTextField;

    private final AddNewTerminalProcessor terminalProcessor = new AddNewTerminalProcessor(); // Direct server-side interaction

    @FXML
    private Button redirectAddTerminalWindowButton;

    @FXML
    private void saveChanges(ActionEvent event) {
        // Get data from input fields
        String terminalId = terminalNoTextField.getText().trim();
        String room = roomTextField.getText().trim();
        String osType = osTypeTextField.getText().trim();
        String status = statusTextField.getText().trim();

        // Validate inputs
        if (terminalId.isEmpty() || room.isEmpty() || osType.isEmpty() || status.isEmpty()) {
            showAlert("Error", "All fields must be filled", Alert.AlertType.ERROR);
            return;
        }

        if (!status.matches("Active|Reserved|Maintenance")) {
            showAlert("Error", "Status must be 'Active', 'Reserved', or 'Maintenance'.", Alert.AlertType.ERROR);
            return;
        }

        // Create model and set its values
        AddNewTerminalModel terminalModel = new AddNewTerminalModel();
        terminalModel.setTerminalId(terminalId);
        terminalModel.setRoom(room);
        terminalModel.setOsType(osType);
        terminalModel.setStatus(status);

        // Process the terminal data using the server-side processor
        boolean success = terminalProcessor.processTerminalData(terminalModel);

        if (success) {
            showAlert("Success", "Terminal added!", Alert.AlertType.INFORMATION);
            closeWindow(event);
        } else {
            showAlert("Error", "Failed to add terminal. Try again.", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void redirectAddTerminalWindow(ActionEvent event) {
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
        if (redirectAddTerminalWindowButton != null) {
            redirectAddTerminalWindowButton.setOnAction(event -> redirectAddTerminalWindow(event));
        }
    }
}
