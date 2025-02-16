package client.admin.model;

import client.utility.ServerConnectionManager;
import client.utility.ServerConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.LogReport;
import server.admin.ReportGeneratorProcessor;

import java.io.IOException;
import java.util.List;

public class ReportGeneratorModel {
    private ServerConnection serverConnection;

    public ReportGeneratorModel () {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<LogReport> loadLogsFromServer() {
        if (serverConnection != null) {
            List<LogReport> logs = ReportGeneratorProcessor.parseLogXML();
            if (logs == null) {
                System.out.println("No terminal data received from server!");
                return FXCollections.observableArrayList();
            }
            return FXCollections.observableArrayList(logs);
        }
        return FXCollections.observableArrayList();
    }
}
