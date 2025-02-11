package client.student.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class RoomItemCardView extends HBox {

    // FXML components
    @FXML
    private Label roomTypeLabel;

    @FXML
    private Label terminalNumberLabel;

    @FXML
    private Button seeTerminalsButton;

    // Constructor
    public RoomItemCardView() {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/student/view/RoomItemCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load RoomItemCard.fxml", e);
        }
    }

    // Getters for the components
    public Label getRoomTypeLabel() {
        return roomTypeLabel;
    }

    public Label getTerminalNumberLabel() {
        return terminalNumberLabel;
    }

    public Button getSeeTerminalsButton() {
        return seeTerminalsButton;
    }

    // Methods to update the view
    public void setRoomType(String roomType) {
        roomTypeLabel.setText(roomType);
    }

    public void setTerminalNumber(int terminalNumber) {
        terminalNumberLabel.setText(String.valueOf(terminalNumber));
    }
}