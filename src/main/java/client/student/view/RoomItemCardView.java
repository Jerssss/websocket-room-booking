package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RoomItemCardView {

    // Variables corresponding to the FXML components
    @FXML
    private Label roomTypeDescription;

    @FXML
    private Label roomTypeLabel;

    @FXML
    private Label availableTerminalsLabel;

    @FXML
    private Label terminalNumberLabel;
    @FXML
    private Button seeTerminalsButton;


    public void setActionSeeTerminalsButton(EventHandler<ActionEvent> event) {
        this.seeTerminalsButton.setOnAction(event);
    }

    // Methods to set data for the card
    public void setRoomName(String roomName) {
        // Assuming the room name is displayed in a label (you may need to add a Label for this in the FXML)
        // roomNameLabel.setText(roomName);
    }
    public void setRoomType(String roomType) {
        roomTypeLabel.setText(roomType);
    }

    public void setAvailableTerminals(int availableTerminals) {
        terminalNumberLabel.setText(String.valueOf(availableTerminals));
    }

    public Button getSeeTerminalsButton() {
        return seeTerminalsButton;
    }
}