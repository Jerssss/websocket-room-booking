package client.admin.view;

import client.admin.controller.ReportGeneratorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import server.utility.LogReport;
import server.utility.ReservationReport;

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
    private Button searchButton;
    @FXML
    private TextField searchReportTextField;

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
        // Initialize ComboBox items
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
        dateColumn11.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        // ComboBox selection listener to toggle search input visibility
        sortByComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Filter by Date".equals(newValue)) {
                searchReportTextField.setVisible(true);
                saveChangesButton.setText("Search");
                centerPane.requestLayout();  // Force layout refresh
            } else {
                searchReportTextField.setVisible(false);
                saveChangesButton.setText("Save Changes");
                centerPane.requestLayout();  // Force layout refresh
            }
            applySortingAndFiltering(newValue);
        });

        // Initialize the search button
        saveChangesButton.setOnAction(event -> applySortingAndFiltering(sortByComboBox.getValue()));

        // Load data if the controller is set
        if (controller != null) {
            controller.loadLogsData();
            controller.loadReservationReports();
        }

        // Bind data to tables
        reservationReportTableView.setItems(reservationReports);
        logReportTableView.setItems(logReports);
    }

    private void applySortingAndFiltering(String sortOption) {
        if (sortOption == null) return;

        String dateFilter = null;
        if ("Filter by Date".equals(sortOption)) {
            dateFilter = searchReportTextField.getText();
        }

        // Delegate the sorting and filtering logic to the controller
        controller.applySortingAndFiltering(sortOption, dateFilter);
    }

    public void setReservationReports(ObservableList<ReservationReport> data) {
        reservationReports.setAll(data);
    }

    public void setLogsData(ObservableList<LogReport> data) {
        logReports.setAll(data);
    }

    // Display error messages using a JavaFX-friendly method
    public void showDateError(String message) {
        showAlert(Alert.AlertType.ERROR, "Error", message);
    }

    public void showNoDataForDate(String message) {
        showAlert(Alert.AlertType.INFORMATION, "No Data", message);
    }

    // Helper method to display alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
