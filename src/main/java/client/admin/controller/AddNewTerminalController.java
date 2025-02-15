package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import client.admin.view.AddNewTerminalView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.admin.AddNewTerminalProcessor;
import server.utility.TerminalVer2;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class AddNewTerminalController {
    private final AddNewTerminalView view;
    private final AddNewTerminalModel model;


    public AddNewTerminalController(AddNewTerminalView view) {
        this.view = view;
        this.model = new AddNewTerminalModel();

        // Set up event handlers
        this.view.setSaveChangesButtonAction(this::handleSaveChange);
    }

    private void handleSaveChange(ActionEvent event) {
        String terminalId = view.getTerminalNoTextField().getText().trim();
        String room = view.getRoomTextField().getText().trim();
        String osType = view.getOsTypeTextField().getText().trim();
        String status = view.getStatusTextField().getText().trim();
        loadDataFromXML("src/main/java/server/util/terminal.xml");

        // Validate inputs
        if (terminalId.isEmpty() || room.isEmpty() || osType.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: All fields must be filled.");
            return;
        }

        if (!status.matches("Active|Reserved|Maintenance")) {
            JOptionPane.showMessageDialog(null, "Error: Status must be 'Active', 'Reserved', or 'Maintenance'");
            return;
        }

        // Create model object
        view.setTerminalId(terminalId);
        view.setRoom(room);
        view.setOsType(osType);
        view.setStatus(status);

        // Process the data
        boolean success = AddNewTerminalProcessor.processTerminalData(
                view.getTerminalId(),
                view.getRoom(),
                view.getOsType(),
                view.getStatus()
        );

        if (success) {
            JOptionPane.showMessageDialog(null, "Success! Terminal has been added!");
            Stage stage = (Stage) view.getSaveChangesButton().getScene().getWindow();
            stage.close();
        } else {
            JOptionPane.showMessageDialog(null, "Error: Failed to create terminal. Try again");
        }
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
            System.out.println("Error loading Login GUI: " + e.getMessage());
        }
    }
    // Method to load data from the XML file
    public static void loadDataFromXML(String filePath) {
        List<TerminalVer2> terminalVer2s = AddNewTerminalProcessor.parseXML(filePath);
        if (terminalVer2s != null) {
            AddNewTerminalView.terminalResults.clear(); // Clear the current data
            AddNewTerminalView.terminalResults.addAll(terminalVer2s);
        }
    }
    public static void refreshTable() {
        String filePath = "src/main/java/server/util/terminal.xml";
        loadDataFromXML(filePath);
    }
}

