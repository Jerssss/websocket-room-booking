package client.student.controller;

import client.signup.SignUpController;
import client.signup.SignUpModel;
import client.student.model.RoomItemCardModel;
import client.student.model.StudentMainMenuModel;
import client.student.view.RoomItemCardView;
import client.student.view.StudentMainMenuView;
import client.student.view.TerminalPickerWindowView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomItemCardController {

    private RoomItemCardView roomItemCardView;
    private RoomItemCardModel roomItemCardModel;
    private String roomId;
    private Parent root;

    // Constructor
    public RoomItemCardController(RoomItemCardView roomItemCardView, RoomItemCardModel roomItemCardModel) {
        this.roomItemCardView = roomItemCardView;
        this.roomItemCardModel = roomItemCardModel;
//        this.roomId = roomId;
        initialize();
    }

    // Initialize the controller
    @FXML
    private void initialize() {

        // Fetch room details from the model and update the view
//        fetchAndDisplayRoomDetails();

        // Set up event handlers
//        roomItemCardView.setActionSeeTerminalsButton(this::handleSeeTerminalsButton);

    }

//    private void handleSeeTerminalsButton(ActionEvent event) {
//        try {
//            // Ensure the path to student_main_menu.fxml is correct
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_picker_window.fxml"));
//            Parent root = fxmlLoader.load();
//
//            // Navigate to the Student Main Menu GUI
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error loading Student Main Menu GUI: " + e.getMessage());
//        }
//    }


//    // Fetch room details from the model and update the view
//    private void fetchAndDisplayRoomDetails() {
//        try {
//            String roomDetails = roomItemCardModel.fetchRoomDetails(roomId);
//            if (roomDetails != null) {
//                // Parse the room details (assuming the response is in a specific format)
//                String[] details = roomDetails.split(",");
//                if (details.length >= 2) {
//                    String roomType = details[0].trim();
//                    int availableTerminals = Integer.parseInt(details[1].trim());
//
//                    // Update the view
//                    roomItemCardView.setRoomType(roomType);
//                    roomItemCardView.setAvailableTerminals(availableTerminals);
//                } else {
//                    System.err.println("Invalid room details format for room: " + roomId);
//                }
//            } else {
//                System.err.println("Failed to fetch room details for room: " + roomId);
//            }
//        } catch (NumberFormatException e) {
//            System.err.println("Error parsing terminal number for room: " + roomId);
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("Unexpected error fetching room details for room: " + roomId);
//            e.printStackTrace();
//        }
//    }

    // Handle "See Terminals" button click
    private void handleSeeTerminalsButton() {
        System.out.println("See Terminals button clicked for room: " + roomId);
        String terminalDetails = roomItemCardModel.fetchTerminalDetails(roomId);
        if (terminalDetails != null) {
            System.out.println("Terminal Details: " + terminalDetails);
            // Add logic to display terminal details (e.g., open a new window or update the UI)
        } else {
            System.err.println("Failed to fetch terminal details for room: " + roomId);
        }
    }
}