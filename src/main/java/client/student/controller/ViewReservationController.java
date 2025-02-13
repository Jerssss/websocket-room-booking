package client.student.controller;

import client.student.model.ViewReservationModel;
import client.student.view.ViewReservationView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class ViewReservationController {
    private final ViewReservationView view;
    private final ViewReservationModel model;

    public ViewReservationController(ViewReservationView view, ViewReservationModel model) {
        this.view = view;
        this.model = model;
        initialize();
    }

    private void initialize() {
        System.out.println("ViewReservationController initialized");
        // Set up event handlers
        view.getRefreshButton().setOnAction(this::handleRefreshButton);

        // Initialize TableView columns
        view.getReservationIDColumn().setCellValueFactory(new PropertyValueFactory<>("reservation_id"));
        view.getTerminalNoColumn().setCellValueFactory(new PropertyValueFactory<>("terminal_id"));
        view.getRoomNoColumn().setCellValueFactory(new PropertyValueFactory<>("terminal_room"));
        view.getReservationStatusColumn().setCellValueFactory(new PropertyValueFactory<>("terminal_status"));
    }

    private void handleRefreshButton(ActionEvent event) {
        System.out.println("Refresh button clicked!");

        // Validate all inputs
        if (!validateAllInputs()) {
            return; // Exit the method if validation fails
        }

        // Fetch reservations from the model
        Map<String, Map<String, String>> reservations = model.fetchAllReservations();

        // Check if reservations are empty
        if (reservations.isEmpty()) {
            showAlert("No Reservations", "No reservations found.");
            return;
        }

        // Convert the reservation data into an ObservableList of Maps
        ObservableList<Map<String, String>> observableReservations = FXCollections.observableArrayList(reservations.values());

        // Update the TableView with the parsed data
        TableView<Map<String, String>> tableView = view.getModResTableView();
        tableView.setItems(observableReservations);
    }

    private boolean validateAllInputs() {
        // Validate time inputs
        String startTime = view.getStartTimeTextField().getText();
        String endTime = view.getEndTimeTextField().getText();
        boolean isValid = true;

        // Check if start time is in the correct format (HH:MM)
        if (!isValidTimeFormat(startTime)) {
            view.getStartTimeTextField().setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            view.getStartTimeTextField().setStyle(""); // Reset style if valid
        }

        // Check if end time is in the correct format (HH:MM)
        if (!isValidTimeFormat(endTime)) {
            view.getEndTimeTextField().setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            view.getEndTimeTextField().setStyle(""); // Reset style if valid
        }

        // Validate time duration
        if (isValid) {
            String[] startParts = startTime.split(":");
            String[] endParts = endTime.split(":");
            int startMinutes = Integer.parseInt(startParts[0]) * 60 + Integer.parseInt(startParts[1]);
            int endMinutes = Integer.parseInt(endParts[0]) * 60 + Integer.parseInt(endParts[1]);
            if (endMinutes <= startMinutes) {
                showAlert("Invalid Time", "End time must be after start time");
                isValid = false;
            }
        }

        // Validate date inputs
        String month = view.getMonthTextField().getText();
        String day = view.getDayTextField().getText();
        String year = view.getYearTextField().getText();

        try {
            int monthNum = Integer.parseInt(month);
            int dayNum = Integer.parseInt(day);
            int yearNum = Integer.parseInt(year);
            if (monthNum < 1 || monthNum > 12 || dayNum < 1 || dayNum > 31 || yearNum < 0 || yearNum > 99) {
                isValid = false;
            }
        } catch (NumberFormatException e) {
            isValid = false;
        }

        return isValid;
    }

    // Helper method to validate time format
    private boolean isValidTimeFormat(String time) {
        if (time == null || time.length() != 5) {
            return false; // Must be in HH:MM format
        }
        String[] parts = time.split(":");
        if (parts.length != 2) {
            return false; // Must have exactly two parts
        }
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return (hours >= 0 && hours < 24) && (minutes >= 0 && minutes < 60);
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}