package client.student.controller;

import client.student.model.RoomItemCardModel;
import client.student.view.RoomItemCardView;
import client.utility.XMLParser;
import javafx.fxml.FXML;

public class RoomItemCardController {

    private RoomItemCardView roomItemCardView;
    private RoomItemCardModel roomItemCardModel;

    public RoomItemCardController(RoomItemCardView roomItemCardView, RoomItemCardModel roomItemCardModel) {
        this.roomItemCardView = roomItemCardView;
        this.roomItemCardModel = roomItemCardModel;
        initialize();
    }

    @FXML
    private void initialize() {
        fetchAndDisplayRoomDetails();
        roomItemCardView.setActionSeeTerminalsButton(event -> handleSeeTerminalsButton());
    }

    private void fetchAndDisplayRoomDetails() {
        String terminalData = roomItemCardModel.fetchAllTerminalDetails();
        if (terminalData != null) {
            String parsedData = XMLParser.parseResponse(terminalData);
            int availableTerminals = countAvailableTerminals(parsedData);
            roomItemCardView.setAvailableTerminals(availableTerminals);
        } else {
            System.err.println("Failed to fetch terminal details.");
        }
    }

    private int countAvailableTerminals(String xmlData) {
        return xmlData.split("<Terminal>").length - 1;
    }

    private void handleSeeTerminalsButton() {
        System.out.println("See Terminals button clicked.");
    }
}