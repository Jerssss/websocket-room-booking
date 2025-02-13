package client.student.controller;

import client.student.model.CreateReservationModel;
import client.student.view.CreateReservationView;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CreateReservationController {
    private final CreateReservationView createReservationView;
    private final CreateReservationModel createReservationModel;

    public CreateReservationController(CreateReservationView createReservationView, CreateReservationModel createReservationModel) {
        this.createReservationView = createReservationView;
        this.createReservationModel = createReservationModel;
        initializeEventHandlers();
    }

    private void initializeEventHandlers() {
        createReservationView.getRefreshButton().setOnAction(this::handleRefreshButton);
        addInputValidationListeners();
    }

    private void addInputValidationListeners() {

    }

    private void handleRefreshButton(ActionEvent event) {
        if (!validateTime(createReservationView.getStartTimeTextField().getText(), createReservationView.getStartTimeTextField()) ||
                !validateTime(createReservationView.getEndTimeTextField().getText(), createReservationView.getEndTimeTextField())) {
            showAlert("Invalid Time", "Please enter a valid time in HH:MM format.");
            return;
        }

        if (!validateTimeDuration()) {
            showAlert("Invalid Duration", "Start time must be earlier than end time, and the duration must not exceed 1 hour.");
            return;
        }

        if (!validateDatePart(createReservationView.getMonthTextField().getText(), createReservationView.getMonthTextField(), 12) ||
                !validateDatePart(createReservationView.getDayTextField().getText(), createReservationView.getDayTextField(), 31) ||
                !validateDatePart(createReservationView.getYearTextField().getText(), createReservationView.getYearTextField(), 99)) {
            showAlert("Invalid Date", "Please enter a valid date in MM/DD/YY format.");
            return;
        }
    }

    private boolean validateTimeDuration() {
        TextField startTimeField = createReservationView.getStartTimeTextField();
        TextField endTimeField = createReservationView.getEndTimeTextField();

        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();

        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        int startTotalMinutes = (startHour * 60) + startMinute;
        int endTotalMinutes = (endHour * 60) + endMinute;
        int duration = endTotalMinutes - startTotalMinutes;

        boolean isValid = duration > 0 && duration <= 60;
        return isValid;
    }

    private boolean validateTime(String time, TextField textField) {
        if (time == null || !time.contains(":")) return false;

        String[] parts = time.split(":");
        if (parts.length != 2) return false;

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateDatePart(String value, TextField textField, int maxValue) {
        try {
            int number = Integer.parseInt(value);
            return number >= 1 && number <= maxValue;
        } catch (NumberFormatException e) {
            return false;
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
