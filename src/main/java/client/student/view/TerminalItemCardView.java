package client.student.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TerminalItemCardView {

    @FXML
    private Label pcName;
    @FXML
    private Label availableTime;  // This must match FXML exactly
    @FXML
    private Label date;
    @FXML
    private Label os;
    @FXML
    private Button reservationButton;

    public void setPcName(String name) {
        if (pcName != null) {
            pcName.setText(name);
        } else {
            System.out.println("pcName is NULL");
        }
    }

    public void setAvailableTime(String time) {
        if (availableTime != null) {
            availableTime.setText(time);
        } else {
            System.out.println("availableTime is NULL");
        }
    }

    public void setDate(String dateText) {
        if (date != null) {
            date.setText(dateText);
        } else {
            System.out.println("date is NULL");
        }
    }

    public void setOS(String osText) {
        if (os != null) {
            os.setText(osText);
        } else {
            System.out.println("os is NULL");
        }
    }

    public void setReservationButtonAction(Runnable action) {
        if (reservationButton != null) {
            reservationButton.setOnAction(e -> action.run());
        } else {
            System.out.println("reservationButton is NULL");
        }
    }

    public void setTimeAvailable(String timeAvailable) {
    }

    public void setAvailability(String status) {

    }
}
