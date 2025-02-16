package client.admin.view;

import client.admin.controller.ReportGeneratorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import server.utility.LogReport;
import server.utility.ReservationReport;

import javax.swing.*;

public class ReportGeneratorView {

    @FXML
    private VBox centerPane;

    @FXML
    private Label reportsLabel;

    @FXML
    private ComboBox<String> sortByComboBox;

    @FXML
    private Button saveChangesButton;

    @FXML
    private TextField dateFilterTextField;

    @FXML
    private TabPane reportsTabPane;

    @FXML
    private Tab logReportTab;

    @FXML
    private TableView<LogReport> logReportTableView;

    @FXML
    private TableColumn<LogReport, String> resIDColumn1;

    @FXML
    private TableColumn<LogReport, String> terminalColumn1;

    @FXML
    private TableColumn<LogReport, String> roomNumberColumn1;

    @FXML
    private TableColumn<LogReport, String> statusColumn1;

    @FXML
    private TableColumn<LogReport, String> statusColumn12;

    @FXML
    private Tab reservationReportTab;

    @FXML
    private TableView<ReservationReport> reservationReportTableView;

    @FXML
    private TableColumn<ReservationReport, String> resIDColumn11;

    @FXML
    private TableColumn<ReservationReport, String> terminalColumn11;

    @FXML
    private TableColumn<ReservationReport, String> roomNumberColumn11;

    @FXML
    private TableColumn<ReservationReport, String> statusColumn11;

    @FXML
    private TableColumn<ReservationReport, String> dateColumn11;

    private ReportGeneratorController controller = new ReportGeneratorController(this);

    private final ObservableList<LogReport> logReports = FXCollections.observableArrayList();
    private final ObservableList<ReservationReport> reservationReports = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        sortByComboBox.getItems().addAll("Sort by Students", "Sort by Admin", "Filter by Date");

        // Set up columns for Log Report
        resIDColumn1.setCellValueFactory(cellData -> cellData.getValue().userIDProperty());
        terminalColumn1.setCellValueFactory(cellData -> cellData.getValue().userTypeProperty());
        roomNumberColumn1.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());
        statusColumn1.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusColumn12.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        // Set up columns for Reservation Report
        resIDColumn11.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        terminalColumn11.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        roomNumberColumn11.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());
        statusColumn11.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusColumn12.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        sortByComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Filter by Date")) {
                dateFilterTextField.setVisible(true);
                saveChangesButton.setText("Search");
                centerPane.requestLayout();  // Force layout refresh
            } else {
                dateFilterTextField.setVisible(false);
                saveChangesButton.setText("Save Changes");
                centerPane.requestLayout();  // Force layout refresh
            }
            applySortingAndFiltering(newValue);
        });


        // Initialize the search button
        saveChangesButton.setOnAction(event -> applySortingAndFiltering(sortByComboBox.getValue()));

        if (controller != null) {
            controller.loadLogsData();
        }

        reservationReportTableView.setItems(reservationReports);
    }

    public void setLogsData(ObservableList<LogReport> data) {
        logReports.setAll(data);
        logReportTableView.setItems(logReports);
    }

    private void applySortingAndFiltering(String sortOption) {
        if (sortOption == null) return;

        String dateFilter = null;

        if (sortOption.equals("Filter by Date")) {
            dateFilter = dateFilterTextField.getText();
        }

        // Delegate the sorting and filtering logic to the controller
        controller.applySortingAndFiltering(sortOption, dateFilter);
    }

    public void showDateError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showNoDataForDate(String message) {
        JOptionPane.showMessageDialog(null, message, "No Data", JOptionPane.INFORMATION_MESSAGE);
    }
}
