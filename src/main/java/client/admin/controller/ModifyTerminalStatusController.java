package client.admin.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import client.admin.view.ModifyTerminalStatusView;
import client.admin.model.ModifyTerminalStatusModel;
import server.utility.Terminal;
import javax.swing.*;
import java.util.stream.Collectors;

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

    public void searchTerminals(String searchText) {
        System.out.println("[DEBUG] Searching for terminals with keyword: " + searchText);

        if (searchText == null || searchText.trim().isEmpty()) {
            view.setTerminalData(terminalData);
            System.out.println("[DEBUG] Search text is empty. Resetting to full terminal list.");
            return;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        ObservableList<Terminal> filteredList = terminalData.stream()
                .filter(terminal ->
                        terminal.getTerminalRoom().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalId().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalOs().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalStatus().toLowerCase().contains(lowerCaseSearchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        view.setTerminalData(filteredList);
        System.out.println("[DEBUG] Search completed. Matching results: " + filteredList.size());
    }

    public void saveChanges() {
        model.saveTerminalData(terminalData); // Save only the current table data
        JOptionPane.showMessageDialog(null, "Changes have been successfully saved!",
                "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}
