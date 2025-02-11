package client.student.controller;

import client.student.model.RoomItemCardModel;
import client.student.view.RoomItemCardView;

public class RoomItemCardController {

    private RoomItemCardView view;
    private RoomItemCardModel model;
    private String roomId;

    // Constructor
    public RoomItemCardController(RoomItemCardView view, RoomItemCardModel model, String roomId) {
        this.view = view;
        this.model = model;
        this.roomId = roomId;
        initialize();
    }

    // Initialize the controller
    private void initialize() {
        // Fetch room details from the model and update the view
        fetchAndDisplayRoomDetails();

        // Set up event handlers
        setupEventHandlers();
    }

    // Fetch room details from the model and update the view
    private void fetchAndDisplayRoomDetails() {
        String roomDetails = model.fetchRoomDetails(roomId);
        if (roomDetails != null) {
            // Parse the room details (assuming the response is in a specific format)
            String[] details = roomDetails.split(",");
            String roomType = details[0];
            int availableTerminals = Integer.parseInt(details[1]);

            // Update the view
            view.setRoomType(roomType);
            view.setTerminalNumber(availableTerminals);
        } else {
            System.err.println("Failed to fetch room details for room: " + roomId);
        }
    }

    // Set up event handlers
    private void setupEventHandlers() {
        view.getSeeTerminalsButton().setOnAction(event -> handleSeeTerminalsButton());
    }

    // Handle "See Terminals" button click
    private void handleSeeTerminalsButton() {
        System.out.println("See Terminals button clicked for room: " + roomId);
        String terminalDetails = model.fetchTerminalDetails(roomId);
        if (terminalDetails != null) {
            System.out.println("Terminal Details: " + terminalDetails);
            // Add logic to display terminal details (e.g., open a new window or update the UI)
        } else {
            System.err.println("Failed to fetch terminal details for room: " + roomId);
        }
    }
}