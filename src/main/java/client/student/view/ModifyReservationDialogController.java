package client.student.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import server.utility.Reservation;

public class ModifyReservationDialogController {
    @FXML private Label reservedDateLabel;
    @FXML private Label reservedTimeLabel;
    @FXML private Label reservationRoomNoLabel;
    @FXML private Label reservationTerminalNoLabel;
    @FXML private Button deleteReservationButton;
    @FXML private Button sendRequestButton;

    private Stage dialogStage;
    private boolean deleteConfirmed = false;
    private Reservation reservation; // Store the reservation being modified

    // Set the reservation details and store the reference
    public void setReservationDetails(Reservation reservation) {
        this.reservation = reservation; // Store the reference
        reservedDateLabel.setText(reservation.getDate());
        reservedTimeLabel.setText(reservation.getStartTime());
        reservationRoomNoLabel.setText(reservation.getRoomNumber());
        reservationTerminalNoLabel.setText(reservation.getTerminalNumber());
    }

    @FXML
    private void handleDelete() {
        deleteConfirmed = true;
        dialogStage.close();
    }

    @FXML
    private void handleSendRequest() {
        // Update the reservation details
        reservation.setStatus("Pending"); // Set status to Pending

        // Close the dialog
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }
}