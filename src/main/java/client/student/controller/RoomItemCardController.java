package client.student.controller;

import client.student.view.RoomItemCardView;
import javafx.fxml.FXML;
import javafx.scene.Parent;

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


    }

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