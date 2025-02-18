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

        ObservableList<Terminal> filteredList = ModifyTerminalProcessor.searchTerminals(terminalData, searchText);
        return filteredList;
    }

    public void saveTerminalData(ObservableList<Terminal> terminalData) {
        if (serverConnection != null) {
            ModifyTerminalProcessor.saveToXML(terminalData);
        }
    }
}
