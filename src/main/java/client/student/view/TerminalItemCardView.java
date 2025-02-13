package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TerminalItemCardView {
    @FXML
    private Label pcName;

    @FXML
    private Label availableTime;

    @FXML
    private Label date;
    @FXML
    private Label os;

    @FXML
    private Button reservationButton;


    public void setPcName(String pcName) {

    }
    public void setItemDescription(String itemDescription) {

    }

    public void setAvailability(String availability) {

    }
    public void setActionSeeTerminalsButton(EventHandler<ActionEvent> event) {
        this.reservationButton.setOnAction(event);
    }

    public Button getSeeTerminalsButton() {
        return reservationButton;
    }
}
