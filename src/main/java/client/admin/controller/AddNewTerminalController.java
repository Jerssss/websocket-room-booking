package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import client.admin.view.AddNewTerminalView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import server.admin.AddNewTerminalProcessor;

public class AddNewTerminalController {
    private final AddNewTerminalView view;
    private final AddNewTerminalProcessor processor;
    private final AddNewTerminalProcessor terminalProcessor = new AddNewTerminalProcessor();


    public AddNewTerminalController(AddNewTerminalView view) {
        this.view = view;
        this.processor = new AddNewTerminalProcessor();

        // Set up event handlers
        this.view.setSaveChangesButtonAction(this::handleSaveChange);
    }

    private void handleSaveChange(ActionEvent event) {
        String terminalId = view.getTerminalNoTextField().getText().trim();
        String room = view.getRoomTextField().getText().trim();
        String osType = view.getOsTypeTextField().getText().trim();
        String status = view.getStatusTextField().getText().trim();

        // Validate inputs
        if (terminalId.isEmpty() || room.isEmpty() || osType.isEmpty() || status.isEmpty()) {
            showAlert("Error", "All fields must be filled", Alert.AlertType.ERROR);
            return;
        }

        if (!status.matches("Active|Reserved|Maintenance")) {
            showAlert("Error", "Status must be 'Active', 'Reserved', or 'Maintenance'.", Alert.AlertType.ERROR);
            return;
        }

        // Create model object
        AddNewTerminalView terminalView = new AddNewTerminalView();
        terminalView.setTerminalId(terminalId);
        terminalView.setRoom(room);
        terminalView.setOsType(osType);
        terminalView.setStatus(status);

        // Process the data
        boolean success = terminalProcessor.processTerminalData(
                terminalView.getTerminalId(),
                terminalView.getRoom(),
                terminalView.getOsType(),
                terminalView.getStatus()
        );

        if (success) {
            showAlert("Success", "Terminal added successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to add terminal. Try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
