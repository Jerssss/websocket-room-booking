package client.student.controller;

import client.student.view.TerminalItemCardView;
import javafx.fxml.FXML;

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
