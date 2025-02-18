package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.admin.ModifyTerminalProcessor;
import server.utility.Terminal;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyTerminalStatusModel {
    private ServerConnection serverConnection;

    public ModifyTerminalStatusModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Terminal> loadTerminalData() {
        if (serverConnection != null) {
            List<Terminal> terminals = ModifyTerminalProcessor.parseXML();
            if (terminals == null) {
                System.out.println("No terminal data received from server!");
                return FXCollections.observableArrayList();
            }
            return FXCollections.observableArrayList(terminals);
        }
        return FXCollections.observableArrayList();
    }

    public ObservableList<Terminal> searchTerminals(ObservableList<Terminal> terminalData, String searchText) {

        if (searchText == null || searchText.trim().isEmpty()) {
            System.out.println("[DEBUG] Search text is empty. Resetting to full terminal list.");
            return terminalData;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        ObservableList<Terminal> filteredList = terminalData.stream()
                .filter(terminal ->
                        terminal.getTerminalRoom().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalId().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalOs().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalStatus().toLowerCase().contains(lowerCaseSearchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        System.out.println("[DEBUG] Search completed. Matching results: " + filteredList.size());
        return filteredList;
    }

    public void saveTerminalData(ObservableList<Terminal> terminalData) {
        if (serverConnection != null) {
            ModifyTerminalProcessor.saveToXML(terminalData);
        }
    }
}
