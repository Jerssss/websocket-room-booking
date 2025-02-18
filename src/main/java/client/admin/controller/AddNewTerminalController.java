package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import client.admin.view.AddNewTerminalView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.admin.AddNewTerminalProcessor;
import server.utility.Terminal;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class AddNewTerminalController {
    private final AddNewTerminalView view;
    private static AddNewTerminalModel model;

    public AddNewTerminalController(AddNewTerminalView view) {
        this.view = view;
        this.model = new AddNewTerminalModel();

        // Set up event handlers
        this.view.setSaveChangesButtonAction(this::handleSaveChange);
    }

    private void handleSaveChange(ActionEvent event) {
        // Get user inputs
        String terminalId = view.getTerminalNoTextField().getText().trim();
        String room = view.getRoomNumberComboBox().getSelectionModel().getSelectedItem();
        String osType = view.getTerminalOSComboBox().getSelectionModel().getSelectedItem();
        String status = view.getStatusComboBox().getSelectionModel().getSelectedItem();
        String selectedTimeRange = view.getTimeComboBox().getSelectionModel().getSelectedItem();
        String reservationDate = view.getDateTextField().getText().trim();

        // Load existing data
        loadDataFromXML("src/main/java/server/util/terminal.xml");

        // Validate inputs
        if (terminalId.isEmpty() || room == null || osType == null || status == null || selectedTimeRange == null || reservationDate.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: All fields must be filled, including time and date.");
            closeWindow();
            return;
        }

        // Ensure Terminal ID is numeric
        if (!terminalId.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Error: Terminal ID must be a number.");
            closeWindow();
            return;
        }

        // Ensure reservation date follows the correct format YYYY-MM-DD
        if (!reservationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Error: Reservation date must be in the format YYYY-MM-DD (e.g., 2025-02-19).");
            closeWindow();
            return;
        }

        // Split time into start and end
        String[] times = selectedTimeRange.split("-");
        if (times.length != 2) {
            JOptionPane.showMessageDialog(null, "Error: Invalid time format.");
            closeWindow();
            return;
        }
        String startTime = times[0].trim();
        String endTime = times[1].trim();

        // **Check for duplicate time slot in the same room and date**
        for (Terminal terminal : AddNewTerminalView.terminalResults) {
            if (terminal.getTerminalRoom().equals(room) &&
                    terminal.getReservationDate().equals(reservationDate) &&
                    terminal.getStartTime().equals(startTime) &&
                    terminal.getEndTime().equals(endTime)) {
                JOptionPane.showMessageDialog(null, "Error: This time slot is already taken for this room!");
                closeWindow();
                return; // Prevent submission
            }
        }

        // **Check for duplicate Terminal ID**
        for (Terminal terminal : AddNewTerminalView.terminalResults) {
            if (terminal.getTerminalId().equals(terminalId)) {
                JOptionPane.showMessageDialog(null, "Error: Terminal ID already exists!");
                closeWindow();
                return; // Prevent submission
            }
        }

        // **Save the new terminal entry**
        boolean success = AddNewTerminalProcessor.processTerminalData(
                terminalId, room, osType, status, reservationDate, selectedTimeRange
        );

        if (success) {
            JOptionPane.showMessageDialog(null, "Success! Terminal has been added.");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Failed to create terminal.");
        }

        closeWindow();
    }



    private void closeWindow() {
        Stage stage = (Stage) view.getSaveChangesButton().getScene().getWindow();
        stage.close();
    }


    public static void redirectAddTerminalWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(AddNewTerminalController.class.getResource("/fxml/admin/add_terminal_window.fxml"));
            Parent root = loader.load();
            AddNewTerminalView view = loader.getController();
            AddNewTerminalController controller = new AddNewTerminalController(view);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Add Terminal GUI: " + e.getMessage());
        }
    }

    // Method to load data from the XML file
    public static void loadDataFromXML(String filePath) {
        List<Terminal> terminal = AddNewTerminalProcessor.parseXML(filePath);
        if (terminal != null) {
            AddNewTerminalView.terminalResults.clear(); // Clear the current data
            AddNewTerminalView.terminalResults.addAll(terminal);
        }
    }

    public static void refreshTable() {
        String filePath = "src/main/java/server/util/terminal.xml";
        loadDataFromXML(filePath);
    }
}
