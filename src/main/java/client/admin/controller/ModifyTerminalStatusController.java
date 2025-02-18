package client.admin.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import client.admin.view.ModifyTerminalStatusView;
import client.admin.model.ModifyTerminalStatusModel;
import server.utility.Terminal;
import javax.swing.*;

public class ModifyTerminalStatusController {
    private final ModifyTerminalStatusView view;
    private final ModifyTerminalStatusModel model;
    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();

    public ModifyTerminalStatusController(ModifyTerminalStatusView view) {
        this.view = view;
        this.model = new ModifyTerminalStatusModel();
    }

    public void loadTerminalData() {
        terminalData = model.loadTerminalData();
        view.setTerminalData(terminalData);
    }

    public void removeTerminal(Terminal terminal) {
        terminalData.remove(terminal); // Remove from local list
        view.setTerminalData(terminalData); // Refresh table with updated list
        System.out.println("[DEBUG] Terminal removed: " + terminal.getTerminalId());
    }

    public void searchTerminals(String searchText){
        ObservableList<Terminal> filteredList = model.searchTerminals(terminalData, searchText);
        view.setTerminalData(filteredList);
    }

    public void saveChanges() {
        model.saveTerminalData(terminalData); // Save only the current table data
        JOptionPane.showMessageDialog(null, "Changes have been successfully saved!",
                "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

}
