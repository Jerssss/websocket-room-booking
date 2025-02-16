package client.admin.model;

import client.utility.ServerConnectionManager;
import client.utility.ServerConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.admin.ReportGeneratorProcessor;
import server.utility.LogReport;
import server.utility.ReservationReport;

import java.io.IOException;
import java.util.List;

public class ReportGeneratorModel {
    private ServerConnection serverConnection;

    public ReportGeneratorModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<LogReport> loadLogsFromServer() {
        List<LogReport> logs = ReportGeneratorProcessor.parseLogXML();
        return FXCollections.observableArrayList(logs);
    }

    public ObservableList<ReservationReport> loadReservationsFromServer() {
        List<ReservationReport> reservations = ReportGeneratorProcessor.parseReservationXML();
        return FXCollections.observableArrayList(reservations);
    }

    public ObservableList<LogReport> searchLogs(String query) {
        List<LogReport> allLogs = ReportGeneratorProcessor.parseLogXML();
        List<LogReport> filteredLogs = ReportGeneratorProcessor.searchLogs(query, allLogs);
        return FXCollections.observableArrayList(filteredLogs);
    }

    public ObservableList<ReservationReport> searchReservations(String query) {
        List<ReservationReport> allReservations = ReportGeneratorProcessor.parseReservationXML();
        List<ReservationReport> filteredReservations = ReportGeneratorProcessor.searchReservations(query, allReservations);
        return FXCollections.observableArrayList(filteredReservations);
    }
}
