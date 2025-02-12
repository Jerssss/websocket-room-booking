package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TerminalPickerWindowView implements Initializable {

    @FXML
    private Label descLabel;
    @FXML
    private Label roomLabel;
    @FXML
    private ScrollPane terminalsScrollPane;
    @FXML
    private AnchorPane terminalsAnchorPane;
    @FXML
    private Button returnButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (returnButton == null) {
            System.out.println("ERROR: returnButton is NULL!");
        } else {
            System.out.println("SUCCESS: returnButton is initialized.");
        }

        // Add return button action
        returnButton.setOnAction(event -> handleClose());
    }

    private void handleClose() {
        System.out.println("Return button clicked. Closing window...");
        // TODO: Implement logic to close the window
    }

    public void setActionReturnButton(EventHandler<ActionEvent> event) {
        returnButton.setOnAction(event);
    }
}
