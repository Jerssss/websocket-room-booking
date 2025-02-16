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
        // Get selected values from combo boxes
        String terminalId = view.getTerminalNoTextField().getText().trim();
        String room = view.getRoomNumberComboBox().getSelectionModel().getSelectedItem();
        String osType = view.getTerminalOSComboBox().getSelectionModel().getSelectedItem();
        String status = view.getStatusComboBox().getSelectionModel().getSelectedItem();
        String selectedDay = view.getDayComboBox().getSelectionModel().getSelectedItem();
        String selectedTime = view.getTimeComboBox().getSelectionModel().getSelectedItem();

        loadDataFromXML("src/main/java/server/util/terminal.xml");

        // Validate inputs
        if (terminalId.isEmpty() || room == null || osType == null || status == null ||
                selectedDay == null || selectedTime == null) {
            JOptionPane.showMessageDialog(null, "Error: All fields must be filled, including day and time.");
            closeWindow(); // Close window even if there's an error
            return;
        }

        // Validate Terminal ID - must be numeric
        if (!terminalId.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Error: Terminal ID must be a number.");
            closeWindow(); // Close window even if there's an error
            return;
        }

        // Create model object
        view.setTerminalId(terminalId);
        view.setRoom(room);
        view.setOsType(osType);
        view.setStatus(status);

        // Process the data with day & time included
        boolean success = AddNewTerminalProcessor.processTerminalData(
                view.getTerminalId(),
                view.getRoom(),
                view.getOsType(),
                view.getStatus(),
                selectedDay,
                selectedTime
        );

        if (success) {
            JOptionPane.showMessageDialog(null, "Success! Terminal has been added!");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Failed to create terminal. Try again");
        }
        closeWindow(); // Always close the window at the end
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
