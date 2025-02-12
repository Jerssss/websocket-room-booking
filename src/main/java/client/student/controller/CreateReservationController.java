package client.student.controller;

import client.student.model.CreateReservationModel;
import client.student.view.CreateReservationView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;

public class CreateReservationController {
    private final CreateReservationView createReservationView;
    private final CreateReservationModel createReservationModel;

    public CreateReservationController(CreateReservationView createReservationView, CreateReservationModel createReservationModel) {
        this.createReservationView = createReservationView;
        this.createReservationModel = createReservationModel;
        initializeEventHandlers();
    }

    // When buttons/text fields detect an action from a user (click/input)
    private void initializeEventHandlers() {
        createReservationView.getRefreshButton().setOnAction(this::handleRefreshButton);
        addInputValidationListeners();
    }

    // Handle refresh button click
    private void handleRefreshButton(ActionEvent event) {
        System.out.println("Refresh button clicked!");

        // Validate time inputs
        if (!validateTime(createReservationView.getStartTimeTextField().getText(), createReservationView.getStartTimeTextField()) ||
                !validateTime(createReservationView.getEndTimeTextField().getText(), createReservationView.getEndTimeTextField())) {
            showAlert("Invalid Time", "Please enter a valid time in HH:MM format.");
            return;
        }

        // Validate time duration
        if (!validateTimeDuration()) {
            showAlert("Invalid Duration", "Start time must be earlier than end time, and the duration must not exceed 1 hour.");
            return;
        }

        // Validate date inputs
        if (!validateDatePart(createReservationView.getMonthTextField().getText(), createReservationView.getMonthTextField(), 12) ||
                !validateDatePart(createReservationView.getDayTextField().getText(), createReservationView.getDayTextField(), 31) ||
                !validateDatePart(createReservationView.getYearTextField().getText(), createReservationView.getYearTextField(), 99)) {
            showAlert("Invalid Date", "Please enter a valid date in MM/DD/YY format.");
            return;
        }

        System.out.println("Finding rooms...");
        GridPane roomsGridPane = createReservationView.getRoomGridPane();
        roomsGridPane.getChildren().clear();
        roomsGridPane.getChildren().add(new Label("Rooms found successfully!"));
    }

    // Add input validation listeners
    private void addInputValidationListeners() {
        createReservationView.getStartTimeTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            validateTime(newValue, createReservationView.getStartTimeTextField());
            validateTimeDuration();
        });

        createReservationView.getEndTimeTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            validateTime(newValue, createReservationView.getEndTimeTextField());
            validateTimeDuration();
        });

        createReservationView.getMonthTextField().textProperty().addListener((observable, oldValue, newValue) ->
                validateDatePart(newValue, createReservationView.getMonthTextField(), 12));

        createReservationView.getDayTextField().textProperty().addListener((observable, oldValue, newValue) ->
                validateDatePart(newValue, createReservationView.getDayTextField(), 31));

        createReservationView.getYearTextField().textProperty().addListener((observable, oldValue, newValue) ->
                validateDatePart(newValue, createReservationView.getYearTextField(), 99));
    }

    // Validate that start time is earlier than end time and within 1-hour limit
    private boolean validateTimeDuration() {
        TextField startTimeField = createReservationView.getStartTimeTextField();
        TextField endTimeField = createReservationView.getEndTimeTextField();

        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();

        if (!validateTime(startTime, startTimeField) || !validateTime(endTime, endTimeField)) {
            return false; // Stop if either time is invalid
        }

        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        // Convert to total minutes
        int startTotalMinutes = (startHour * 60) + startMinute;
        int endTotalMinutes = (endHour * 60) + endMinute;
        int duration = endTotalMinutes - startTotalMinutes;

        boolean isValid = duration > 0 && duration <= 60;
        startTimeField.setStyle(isValid ? "" : "-fx-border-color: red;");
        endTimeField.setStyle(isValid ? "" : "-fx-border-color: red;");

        return isValid;
    }

    // Validate time input (HH:MM format)
    private boolean validateTime(String time, TextField textField) {
        if (time == null || !time.contains(":")) {
            textField.setStyle("-fx-border-color: red;");
            return false;
        }

        String[] parts = time.split(":");
        if (parts.length != 2) {
            textField.setStyle("-fx-border-color: red;");
            return false;
        }

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            boolean isValid = hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
            textField.setStyle(isValid ? "" : "-fx-border-color: red;");
            return isValid;
        } catch (NumberFormatException e) {
            textField.setStyle("-fx-border-color: red;");
            return false;
        }
    }

    // Validate date part input
    private boolean validateDatePart(String value, TextField textField, int maxValue) {
        try {
            int number = Integer.parseInt(value);
            boolean isValid = number >= 1 && number <= maxValue;

            textField.setStyle(isValid ? "" : "-fx-border-color: red;");
            if (!isValid) {
                showAlert("Invalid Input", "Please enter a number between 1 and " + maxValue + ".");
            }
            return isValid;
        } catch (NumberFormatException e) {
            textField.setStyle("-fx-border-color: red;");
            showAlert("Invalid Input", "Please enter a valid number.");
            return false;
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
