package client.admin.controller;

import client.admin.model.ReportGeneratorModel;
import client.admin.view.ReportGeneratorView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.admin.ReportGeneratorProcessor;
import server.utility.LogReport;
import server.utility.ReservationReport;

import java.util.List;

public class ReportGeneratorController {

    private final ReportGeneratorModel model;
    private final ReportGeneratorView view;

    public ReportGeneratorController(ReportGeneratorView view) {
        this.model = new ReportGeneratorModel();
        this.view = view;
    }

    public void loadLogsData() {
        ObservableList<LogReport> logs = model.loadLogsFromServer();
        view.setLogsData(logs);
    }

    public void loadReservationReports() {
        ObservableList<ReservationReport> reservations = model.loadReservationsFromServer();
        view.setReservationReports(reservations);
    }

    public void applySortingAndFiltering(String sortOption, String dateFilter) {
        List<LogReport> logs = model.loadLogsFromServer();
        List<ReservationReport> reservations = model.loadReservationsFromServer();

        // Apply sorting and filtering for logs
        List<LogReport> sortedAndFilteredLogs = ReportGeneratorProcessor.applySortingAndFiltering(logs, sortOption, dateFilter);

        // Apply sorting and filtering for reservations
        List<ReservationReport> sortedAndFilteredReservations = ReportGeneratorProcessor.applySortingAndFilteringForReservations(reservations, sortOption, dateFilter);

        // Update view with the sorted and filtered logs and reservations
        ObservableList<LogReport> observableLogs = FXCollections.observableArrayList(sortedAndFilteredLogs);
        ObservableList<ReservationReport> observableReservations = FXCollections.observableArrayList(sortedAndFilteredReservations);

        view.setLogsData(observableLogs);
        view.setReservationReports(observableReservations);
    }
}
