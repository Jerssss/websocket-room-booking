package client.admin.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import client.admin.view.ModifyTerminalStatusView;
import server.admin.ModifyTerminalProcessor;
import server.utility.Terminal;

import javax.swing.*;
import java.util.List;

public class ModifyTerminalStatusController {

    private final ModifyTerminalStatusView view;
    private final ModifyTerminalProcessor processor;
    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();

    public ModifyTerminalStatusController(ModifyTerminalStatusView view) {
        this.view = view;
        this.processor = new ModifyTerminalProcessor();
    }

    public void loadTerminalData() {
        List<Terminal> terminals = processor.parseXML("src/main/java/server/util/terminal.xml");
        terminalData.setAll(terminals);
        view.setTerminalData(terminalData);
    }

    public void saveChanges() {
        processor.saveToXML("src/main/java/server/util/terminal.xml", terminalData);
        JOptionPane.showMessageDialog(null, "Changes have been successfully saved!",
                "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}
