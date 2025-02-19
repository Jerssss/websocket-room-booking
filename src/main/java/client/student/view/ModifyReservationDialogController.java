package client.student.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import server.utility.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ModifyReservationDialogController {
    @FXML private Label reservedDateLabel;
    @FXML private Label startTimeLabel;
    @FXML private Label endTimeLabel;
    @FXML private Label reservationRoomNoLabel;
    @FXML private Label reservationTerminalNoLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private ComboBox<String> roomNumberComboBox;
    @FXML private TextField terminalNumberTextField;

    private Stage dialogStage;
    private Reservation originalReservation;
    private Reservation modifiedReservation;
    private boolean deleteConfirmed = false;
    private boolean changesMade = false;
    private List<Reservation> existingReservations;

    @FXML
    public void initialize() {
        setupDateRestrictions();
        setupTerminalValidation();
        roomNumberComboBox.getItems().addAll("D524", "D526", "D426");
    }

    private void setupDateRestrictions() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today) || date.isAfter(today.plusMonths(1)));
            }
        });
    }

    private void setupTerminalValidation() {
        Pattern numberPattern = Pattern.compile("\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (numberPattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        });
        terminalNumberTextField.setTextFormatter(formatter);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.setOnCloseRequest(event -> {
            changesMade = false;
            dialogStage.close();
        });
    }

    public void setReservationDetails(Reservation reservation) {
        this.originalReservation = new Reservation(reservation);
        this.modifiedReservation = reservation;

        reservedDateLabel.setText(originalReservation.getDate());
        startTimeLabel.setText(originalReservation.getStartTime());
        endTimeLabel.setText(originalReservation.getEndTime());
        reservationRoomNoLabel.setText(originalReservation.getRoomNumber());
        reservationTerminalNoLabel.setText(originalReservation.getTerminalNumber());

        datePicker.setValue(LocalDate.parse(originalReservation.getDate()));
        startTimeTextField.setText(originalReservation.getStartTime());
        endTimeTextField.setText(originalReservation.getEndTime());
        roomNumberComboBox.getSelectionModel().select(originalReservation.getRoomNumber());
        terminalNumberTextField.setText(originalReservation.getTerminalNumber());
    }

    public void setExistingReservations(List<Reservation> reservations) {
        this.existingReservations = new ArrayList<>(reservations);
    }

    @FXML
    private void handleSendRequest() {
        List<String> errors = new ArrayList<>();
        LocalDate today = LocalDate.now();
        boolean clearTimeFields = false;
        boolean clearDateField = false;

        // Date validations
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            errors.add("Invalid date format (use yyyy-mm-dd)");
            clearDateField = true;
        } else {
            if (selectedDate.isBefore(today)) {
                errors.add("Cannot reserve in the past");
                clearDateField = true;
            }
            if (selectedDate.isAfter(today.plusMonths(1))) {
                errors.add("Maximum reservation window is 1 month");
                clearDateField = true;
            }
        }

        // Time validations
        LocalTime startTime = parseTime(startTimeTextField.getText(), errors, "start time");
        LocalTime endTime = parseTime(endTimeTextField.getText(), errors, "end time");

        if (startTime != null && endTime != null) {
            if (!endTime.isAfter(startTime)) {
                errors.add("End time must be after start time");
                clearTimeFields = true;
            }

            if (startTime.plusHours(2).isBefore(endTime)) {
                errors.add("Maximum reservation duration is 2 hours");
                clearTimeFields = true;
            }
        }

        // Terminal number validation
        if (!terminalNumberTextField.getText().matches("\\d+")) {
            errors.add("Terminal number must be a number");
            terminalNumberTextField.clear();
        }

        // Overlap validation
        if (selectedDate != null && startTime != null && endTime != null) {
            if (hasTerminalOverlap(selectedDate, startTime, endTime)) {
                errors.add("This terminal is already reserved during selected time");
                clearTimeFields = true;
            }
        }

        // Check 24-hour rule for modification
        LocalDate originalDate = LocalDate.parse(originalReservation.getDate());
        LocalTime originalStartTime = LocalTime.parse(originalReservation.getStartTime());
        LocalDateTime originalDateTime = LocalDateTime.of(originalDate, originalStartTime);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(originalDateTime.minusHours(24))) {
            errors.add("Cannot modify reservation within 24 hours of the original start time");
        }

        // Handle errors
        if (!errors.isEmpty()) {
            if (clearDateField) datePicker.setValue(null);
            if (clearTimeFields) {
                startTimeTextField.clear();
                endTimeTextField.clear();
            }
            showErrorDialog(errors);
            return;
        }

        // Proceed if valid
        if (hasChanges()) {
            updateReservation();
            changesMade = true;
        }
        dialogStage.close();
    }

    private LocalTime parseTime(String time, List<String> errors, String fieldName) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            errors.add("Invalid " + fieldName + " format (use HH:mm)");
            return null;
        }
    }

    private boolean hasTerminalOverlap(LocalDate date, LocalTime newStart, LocalTime newEnd) {
        return existingReservations.stream()
                .filter(res -> res.getRoomNumber().equals(roomNumberComboBox.getValue()))
                .filter(res -> res.getTerminalNumber().equals(terminalNumberTextField.getText()))
                .filter(res -> !res.getReservationId().equals(modifiedReservation.getReservationId()))
                .filter(res -> LocalDate.parse(res.getDate()).equals(date))
                .anyMatch(res -> {
                    LocalTime existingStart = LocalTime.parse(res.getStartTime());
                    LocalTime existingEnd = LocalTime.parse(res.getEndTime());
                    return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
                });
    }

    private void showErrorDialog(List<String> errors) {
        StringBuilder message = new StringBuilder("Validation errors:\n");
        for (String error : errors) {
            message.append("• ").append(error).append("\n");
        }
        JOptionPane.showMessageDialog(null, message.toString(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    private boolean hasChanges() {
        return !originalReservation.getDate().equals(getCurrentDate()) ||
                !originalReservation.getStartTime().equals(startTimeTextField.getText()) ||
                !originalReservation.getEndTime().equals(endTimeTextField.getText()) ||
                !originalReservation.getRoomNumber().equals(roomNumberComboBox.getValue()) ||
                !originalReservation.getTerminalNumber().equals(terminalNumberTextField.getText());
    }

    private String getCurrentDate() {
        return datePicker.getValue() != null ? datePicker.getValue().toString() : "";
    }

    private void updateReservation() {
        modifiedReservation.setDate(getCurrentDate());
        modifiedReservation.setStartTime(startTimeTextField.getText());
        modifiedReservation.setEndTime(endTimeTextField.getText());
        modifiedReservation.setRoomNumber(roomNumberComboBox.getValue());
        modifiedReservation.setTerminalNumber(terminalNumberTextField.getText());

        // Only set to Pending if it's a new modification
        if (!"Pending".equals(modifiedReservation.getStatus())) {
            modifiedReservation.setStatus("Pending");
        }
    }

    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }

    public boolean isChangesMade() {
        return changesMade;
    }
}