package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML
    private GridPane terminalGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (returnButton == null) {
            System.out.println("ERROR: returnButton is NULL!");
        } else {
            System.out.println("SUCCESS: returnButton is initialized.");
        }

        // Add return button action
        returnButton.setOnAction(event ->  handleRefreshButton());

    }

    private void handleClose() {
        System.out.println("Return button clicked. Closing window...");
        // TODO: Implement logic to close the window
    }
    private void handleRefreshButton() {
        // Clear existing cards
        terminalGridPane.getChildren().clear();

        // Add new room cards (example data)
        addRoomCard("D522", "Mac", "1");
        addRoomCard("D522", "Mac", "1");
        addRoomCard("D522", "Mac", "1");
        addRoomCard("D522", "Mac", "1");


    }

    // Method to load and add room cards
    public void addRoomCard(String roomName, String roomType, String availableTerminals) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_item_card.fxml"));
            HBox roomCard = loader.load();

            TerminalItemCardView controller = loader.getController();
            controller.setPcName(roomName);
            controller.setItemDescription(roomType);
            controller.setAvailability(availableTerminals);

    /*        controller.setActionSeeTerminalsButton((ActionEvent event) -> {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_picker_window.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            });*/

            // Calculate row and column indices (3 columns per row)
            int totalCards = terminalGridPane.getChildren().size();
            int columnIndex = totalCards % 2; // Columns: 0, 1,
            int rowIndex = totalCards / 2;    // Rows increment after 2 cards

            // Add the card to the GridPane
            terminalGridPane.add(roomCard, columnIndex, rowIndex);

            // Add margin for spacing
            GridPane.setMargin(roomCard, new Insets(5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setActionReturnButton(EventHandler<ActionEvent> event) {
        returnButton.setOnAction(event);
    }
}
