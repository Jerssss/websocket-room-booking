package client.student.view;

import client.admin.view.AdminMainMenuView;
import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.signup.SignUpController;
import client.signup.SignUpModel;
import client.student.model.CreateReservationModel;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
        roomGridPane.getChildren().clear(); // Clear previous cards

        Map<String, Map<String, Integer>> roomData = CreateReservationModel.parseTerminals("out/production/9444-team1_preproject/resources/data/Terminals.xml");

        int totalCards = 0; // Track number of cards

        for (Map.Entry<String, Map<String, Integer>> roomEntry : roomData.entrySet()) {
            String roomName = roomEntry.getKey();
            Map<String, Integer> osCounts = roomEntry.getValue();

            for (Map.Entry<String, Integer> osEntry : osCounts.entrySet()) {
                String os = osEntry.getKey();
                int availableTerminals = osEntry.getValue();

                addRoomCard(roomName, os, availableTerminals);

                totalCards++; // Increment card count
            }
        }

        System.out.println("Total Room Cards Added: " + totalCards);
        roomGridPane.requestLayout();
    }


    // Method to load and add room cards
    public void addRoomCard(String roomName, String roomType, int availableTerminals) {
        try {
            // Load the room card FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/room_item_card.fxml"));
            HBox roomCard = loader.load();

            // Get the controller for the room card
            RoomItemCardView controller = loader.getController();

            // Set room details
            controller.setRoomName(roomName); // Set the room name
            controller.setRoomType(roomType); // Set the room type (OS)
            controller.setAvailableTerminals(availableTerminals); // Set the number of available terminals

            // Set the action for the "See Terminals" button
            controller.setActionSeeTerminalsButton((ActionEvent event) -> {
                try {
                    // Load the Terminal Picker FXML
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_picker_window.fxml"));
                    Parent root = fxmlLoader.load();

                    // Get the controller of TerminalPickerWindowView
                    TerminalPickerWindowView terminalPickerController = fxmlLoader.getController();

                    // Set the room name dynamically
                    terminalPickerController.setRoomName(roomName);  // roomName should be the selected room

                    // Show the window
                    Stage stage = new Stage();
                    stage.setTitle("Terminal Picker");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });



            // Calculate row and column indices (2 columns per row)
            int totalCards = roomGridPane.getChildren().size();
            int columnIndex = totalCards % 2; // Columns: 0, 1
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