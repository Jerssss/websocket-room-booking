package client.student.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class TerminalItemCardView {

    @FXML
    private Label pcName;
    @FXML
    private Label timeAvailableLabel;
    @FXML
    private Label dateAvailableLabel;
    @FXML
    private Label os;
    @FXML
    private Button addTerminalButton;
    @FXML
    private ImageView pcImage;

    // Setters for the data
    public void setTerminalCardData(String pcNameText, String availableTime, String date, String osText) {
        pcName.setText(pcNameText);
        timeAvailableLabel.setText(availableTime);
        dateAvailableLabel.setText(date);
        os.setText(osText);
    }

    // Handle button click
    @FXML
    private void handleAddTerminalButtonClick() {
        // Add your functionality for button click here
        System.out.println("Terminal Added");
    }

    // Getter for the add terminal button
    public Button getAddTerminalButton() {
        return addTerminalButton;
    }

    // Setter for the add terminal button
    public void setAddTerminalButton(Button addTerminalButton) {
        this.addTerminalButton = addTerminalButton;
    }

    // Setter for PC name
    public void setPcName(String pcNameText) {
        this.pcName.setText(pcNameText);
    }

    // Setter for available time
    public void setTimeAvailable(String timeAvailable) {
        this.timeAvailableLabel.setText(timeAvailable); // Set the current day's availability
    }
    // Setter for OS
    public void setOS(String osText) {
        this.os.setText(osText);
    }

    // Setter for availability status

    // Setter for the current date
    public void setCurrentDate(String currentDate) {
        this.dateAvailableLabel.setText(currentDate);
    }
}
