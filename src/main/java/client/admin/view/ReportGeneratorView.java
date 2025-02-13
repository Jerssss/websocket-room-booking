package client.admin.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
    private TableView<?> logReportTableView;

    @FXML
    private TableColumn<?, ?> resIDColumn1;

    @FXML
    private TableColumn<?, ?> terminalColumn1;

    @FXML
    private TableColumn<?, ?> roomNumberColumn1;

    @FXML
    private TableColumn<?, ?> statusColumn1;

    @FXML
    private TableColumn<?, ?> statusColumn12;

    @FXML
    private Tab reservationReportTab;

    @FXML
    private TableView<?> reservationReportTableView;

    @FXML
    private TableColumn<?, ?> resIDColumn11;

    @FXML
    private TableColumn<?, ?> terminalColumn11;

    @FXML
    private TableColumn<?, ?> roomNumberColumn11;

    @FXML
    private TableColumn<?, ?> statusColumn11;

    @FXML
    private TableColumn<?, ?> statusColumn111;

    @FXML
    public void initialize() {
        // Initialize view elements if necessary
    }

    public ComboBox<String> getSortByComboBox() {
        return sortByComboBox;
    }

    public Button getSaveReportButton() {
        return saveReportButton;
    }

    public TableView<?> getLogReportTableView() {
        return logReportTableView;
    }

    public TableView<?> getReservationReportTableView() {
        return reservationReportTableView;
    }
}
