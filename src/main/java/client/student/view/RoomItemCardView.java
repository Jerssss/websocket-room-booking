package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RoomItemCardView {

    @FXML
    private Label roomTypeLabel;

    @FXML
    private Label availableTerminalsLabel;

    @FXML
    private Button seeTerminalsButton;

    @FXML
    private Label roomTypeDescriptionLabel;

    public void setActionSeeTerminalsButton(EventHandler<ActionEvent> event) {
        this.seeTerminalsButton.setOnAction(event);
    }

    public void setRoomType(String roomType) {
        roomTypeLabel.setText(roomType);
    }

    public void setAvailableTerminals(int availableTerminals) {
        availableTerminalsLabel.setText(String.valueOf(availableTerminals));
    }

    public Button getSeeTerminalsButton() {
        return seeTerminalsButton;
    }

    public void setRoomName(String roomName) {
        roomTypeDescriptionLabel.setText(roomName);
    }
}