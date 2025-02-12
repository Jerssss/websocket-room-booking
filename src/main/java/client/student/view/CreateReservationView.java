package client.student.view;

import client.signup.SignUpController;
import client.signup.SignUpModel;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateReservationView implements Initializable {

    // Variables corresponding to the FXML components
    @FXML
    private VBox centerPane;

    @FXML
    private Label roomsLabel;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private TextField monthTextField;

    @FXML
    private TextField dayTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private Label dateLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private ScrollPane roomsScrollPane;

    @FXML
    private GridPane roomGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Allow GridPane to grow indefinitely
        roomGridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        roomGridPane.setHgap(10);
        roomGridPane.setVgap(10);
        // Configure ScrollPane to fit-to-width
        roomsScrollPane.setFitToWidth(true);
        // Set up event handler for the refresh button
        refreshButton.setOnAction(event -> handleRefreshButton());

    }

    // Handle refresh button click
    private void handleRefreshButton() {
        // Clear existing cards
        roomGridPane.getChildren().clear();

        // Add new room cards (example data)
        addRoomCard("D522", "Mac", 10);
        addRoomCard("D523", "Windows", 5);
        addRoomCard("D524", "Linux", 8);
        addRoomCard("D522", "Mac", 10);
        addRoomCard("D523", "Windows", 5);
        addRoomCard("D524", "Linux", 8);
        addRoomCard("D522", "Mac", 10);
        addRoomCard("D523", "Windows", 5);
        addRoomCard("D524", "Linux", 8);
    }

    // Method to load and add room cards
    public void addRoomCard(String roomName, String roomType, int availableTerminals) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/room_item_card.fxml"));
            HBox roomCard = loader.load();

            RoomItemCardView controller = loader.getController();
            controller.setRoomName(roomName);
            controller.setRoomType(roomType);
            controller.setAvailableTerminals(availableTerminals);

            controller.setActionSeeTerminalsButton((ActionEvent event) -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_picker_window.fxml"));
                    Parent terminalPickerView = fxmlLoader.load();

                    // Find the topmost container (going up one level)
                    Node source = (Node) event.getSource();
                    GridPane parentContainer = (GridPane) source.getScene().lookup("#roomGridPane"); // ID of the container

                    if (parentContainer != null) {
                        parentContainer.getChildren().setAll(terminalPickerView); // Swap view
                    } else {
                        System.out.println("ERROR: Parent container not found!");
                    }

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            });

            // Calculate row and column indices (3 columns per row)
            int totalCards = roomGridPane.getChildren().size();
            int columnIndex = totalCards % 2; // Columns: 0, 1,
            int rowIndex = totalCards / 2;    // Rows increment after 2 cards

            // Add the card to the GridPane
            roomGridPane.add(roomCard, columnIndex, rowIndex);

            // Add margin for spacing
            GridPane.setMargin(roomCard, new Insets(5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Getters for the components
    public VBox getCenterPane() {
        return centerPane;
    }

    public Label getRoomsLabel() {
        return roomsLabel;
    }

    public TextField getStartTimeTextField() {
        return startTimeTextField;
    }

    public TextField getEndTimeTextField() {
        return endTimeTextField;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public TextField getMonthTextField() {
        return monthTextField;
    }

    public TextField getDayTextField() {
        return dayTextField;
    }

    public TextField getYearTextField() {
        return yearTextField;
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public Button getRefreshButton() {

        return refreshButton;
    }

    public ScrollPane getRoomsScrollPane() {
        return roomsScrollPane;
    }

    public GridPane getRoomGridPane() {
        return roomGridPane;
    }
}