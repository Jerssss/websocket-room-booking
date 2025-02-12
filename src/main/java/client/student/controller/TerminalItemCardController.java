package client.student.controller;

import client.student.model.RoomItemCardModel;
import client.student.model.TerminalItemCardModel;
import client.student.view.RoomItemCardView;
import client.student.view.TerminalItemCardView;
import javafx.fxml.FXML;
import javafx.scene.Parent;

public class TerminalItemCardController {

    private TerminalItemCardView terminalItemCardView;
    private TerminalItemCardModel terminalItemCardModel;


    // Constructor
    public TerminalItemCardController(TerminalItemCardView terminalItemCardView, TerminalItemCardModel terminalItemCardModel) {
        this.terminalItemCardView = terminalItemCardView;
        this.terminalItemCardModel = terminalItemCardModel;
//        this.roomId = roomId;
        initialize();
    }

    // Initialize the controller
    @FXML
    private void initialize() {


    }

}
