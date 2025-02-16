package client.admin.controller;

import client.admin.model.ReportGeneratorModel;
import client.admin.view.ReportGeneratorView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.admin.ReportGeneratorProcessor;
import server.utility.LogReport;

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

    public void applySortingAndFiltering(String sortOption, String dateFilter) {
        // Get logs from the model (assuming you already have the logs loaded in the model)
        List<LogReport> logs = model.loadLogsFromServer();

        // Apply sorting and filtering via the processor
        List<LogReport> sortedAndFilteredLogs = ReportGeneratorProcessor.applySortingAndFiltering(logs, sortOption, dateFilter);

        if (sortOption.equals("Filter by Date")) {
            if (sortedAndFilteredLogs.isEmpty()) {
                view.showNoDataForDate("No data found for the entered date.");
            } else {
                ObservableList<LogReport> observableLogs = FXCollections.observableArrayList(sortedAndFilteredLogs);
                view.setLogsData(observableLogs);
            }
        } else {
            ObservableList<LogReport> observableLogs = FXCollections.observableArrayList(sortedAndFilteredLogs);
            view.setLogsData(observableLogs);
        }
    }
}
