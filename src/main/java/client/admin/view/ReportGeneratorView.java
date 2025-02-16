package client.admin.view;

import client.admin.controller.ModifyTerminalStatusController;
import client.admin.controller.ReportGeneratorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
    private Button saveReportButton;

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

        if (controller != null) {
            controller.loadLogsData();
        }

        // Manually add test data
        reservationReports.add(new ReservationReport("RES456", "T002", "Room 102", "Reserved", "2025-02-16"));
        reservationReportTableView.setItems(reservationReports);

    }

    public void setLogsData(ObservableList<LogReport> data) {
        logReports.setAll(data);
        logReportTableView.setItems(logReports);
    }
}
