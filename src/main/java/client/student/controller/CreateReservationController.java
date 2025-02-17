package client.student.controller;

import client.student.model.CreateReservationModel;
import client.student.view.CreateReservationView;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import server.student.CreateReservationProcessor;
import server.utility.Terminal;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class CreateReservationController {

    private final CreateReservationView view;
    private final CreateReservationModel model;


    public CreateReservationController(CreateReservationView view) {
        this.view = view;
        this.model = new CreateReservationModel();
        // Set up event handlers
    //    this.view.setSaveChangesButtonAction(this::handleSaveChange);
    }


    private void closeWindow() {
        Stage stage = (Stage) view.getSaveChangesButton().getScene().getWindow();
        stage.close();
    }

    private void handleSaveChange(ActionEvent event) {
        // Get selected values from combo boxes

        String terminalId = view.getTerminalNoTextField().getText().trim();
        String room = view.getRoomNumberComboBox().getSelectionModel().getSelectedItem();
        String osType = view.getTerminalOSComboBox().getSelectionModel().getSelectedItem();
        String selectedDay = view.getDayComboBox().getSelectionModel().getSelectedItem();
        String selectedTime = view.getTimeComboBox().getSelectionModel().getSelectedItem();

        loadDataFromXML("src/main/java/server/util/terminal.xml");

        // Validate inputs
        if (terminalId.isEmpty() || room == null || osType == null ||
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

        // Process the data with day & time included
        boolean success = CreateReservationProcessor.processReservationData(
                view.getTerminalId(),
                view.getRoom(),
                view.getOsType(),
                selectedDay,
                selectedTime
        );

        if (success) {
            JOptionPane.showMessageDialog(null, "Success! Reservation has been added!");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Failed to create reservation. Try again");
        }
        closeWindow(); // Always close the window at the end
    }

    public static void redirectCreateReservationWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(CreateReservationController.class.getResource("/fxml/client/add_reservation_window.fxml"));
            Parent root = loader.load();
            CreateReservationView view = loader.getController();
            new CreateReservationController(view);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Create Reservation GUI: " + e.getMessage());
        }
    }

    // Method to load data from the XML file
    public static void loadDataFromXML(String filePath) {
        List<Terminal> reservation = CreateReservationProcessor.parseXML(filePath);
        if (reservation != null) {
            CreateReservationView.reservationData.clear(); // Clear the current data
            CreateReservationView.reservationData.addAll(reservation);
        }
    }

    public static void refreshTable() {
        String filePath = "src/main/java/server/util/terminal.xml";
        loadDataFromXML(filePath);
    }
}
