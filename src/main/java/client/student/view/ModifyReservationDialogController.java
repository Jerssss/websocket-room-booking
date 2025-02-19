package client.student.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import server.utility.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
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
        roomNumberComboBox.getItems().addAll("D524", "D526", "D426");
        setupInputValidations();
    }

    private void setupInputValidations() {
        // Terminal number - numbers only
        Pattern terminalPattern = Pattern.compile("\\d*");
        UnaryOperator<TextFormatter.Change> terminalFilter = change -> {
            if (terminalPattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        terminalNumberTextField.setTextFormatter(new TextFormatter<>(terminalFilter));
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
        boolean clearTimeFields = false;
        boolean clearDateField = false;

        // Validate date
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            errors.add("Invalid date format (use yyyy-mm-dd)");
            clearDateField = true;
        }

        // Validate times
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

        // Validate terminal number
        if (!terminalNumberTextField.getText().matches("\\d+")) {
            errors.add("Terminal number must be a number");
            terminalNumberTextField.clear();
        }

        // Check for overlaps
        if (selectedDate != null && startTime != null && endTime != null) {
            if (hasTimeOverlap(selectedDate, startTime, endTime)) {
                errors.add("This reservation overlaps with an existing reservation");
                clearTimeFields = true;
            }
        }

        if (!errors.isEmpty()) {
            if (clearDateField) datePicker.setValue(null);
            if (clearTimeFields) {
                startTimeTextField.clear();
                endTimeTextField.clear();
            }
            showErrors(errors);
            return;
        }

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

    private boolean hasTimeOverlap(LocalDate date, LocalTime newStart, LocalTime newEnd) {
        return existingReservations.stream()
                .filter(res -> res.getRoomNumber().equals(roomNumberComboBox.getValue()))
                .filter(res -> res.getReservationId().equals(modifiedReservation.getReservationId()))
                .filter(res -> LocalDate.parse(res.getDate()).equals(date))
                .anyMatch(res -> {
                    LocalTime existingStart = LocalTime.parse(res.getStartTime());
                    LocalTime existingEnd = LocalTime.parse(res.getEndTime());
                    return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
                });
    }

    private void showErrors(List<String> errors) {
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
        modifiedReservation.setStatus("Pending");
    }

    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }

    public boolean isChangesMade() {
        return changesMade;
    }
}