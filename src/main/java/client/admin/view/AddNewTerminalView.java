package client.admin.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

import client.admin.controller.AddNewTerminalController;
import server.utility.Terminal;

public class AddNewTerminalView {
    private AddNewTerminalController controller;

    @FXML
    private Button saveChangesButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button redirectAddTerminalWindowButton;

    @FXML
    private TextField terminalNoTextField;

    @FXML
    private TextField roomTextField;

    @FXML
    private TextField osTypeTextField;

    @FXML
    private TextField statusTextField;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> timeComboBox;
    @FXML
    private ComboBox<String> terminalOSComboBox;
    @FXML
    private ComboBox<String> roomNumberComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    private String terminalId;
    private String room;
    private String osType;
    private String status;

    // Getters for UI fields
    public TextField getTerminalNoTextField() {
        return terminalNoTextField;
    }

    public TextField getRoomTextField() {
        return roomTextField;
    }

    public TextField getOsTypeTextField() {
        return osTypeTextField;
    }

    public TextField getStatusTextField() {
        return statusTextField;
    }
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @FXML
    private TableView<Terminal> addTerminalTableView;
    @FXML
    private TableColumn<Terminal, String> terminalColumn;
    @FXML
    private TableColumn<Terminal, String> roomNumberColumn;
    @FXML
    private TableColumn<Terminal, String> terminalOSColumn;
    @FXML
    private TableColumn<Terminal, String> dayColumn;
    @FXML
    private TableColumn<Terminal, String> timeColumn;
    @FXML
    private TableColumn<Terminal, String> statusColumn;
    @FXML
    public static ObservableList<Terminal> terminalResults = FXCollections.observableArrayList();

    // Setters for button actions
    public void setSaveChangesButtonAction(EventHandler<ActionEvent> handler) {
        saveChangesButton.setOnAction(handler);
    }

    public Button getSaveChangesButton() {
        return saveChangesButton;
    }

    public void setController(AddNewTerminalController controller) {
        this.controller = controller;
    }
    @FXML
    public void initialize() {
        if (redirectAddTerminalWindowButton != null) {
            redirectAddTerminalWindowButton.setOnAction(event -> AddNewTerminalController.redirectAddTerminalWindow(event));
        }
        Platform.runLater(() -> {
            if (timeComboBox != null) {
                ObservableList<String> timeOptions = FXCollections.observableArrayList(
                        "09:30-17:30",
                        "11:30-16:30",
                        "07:30-15:30"
                );
                timeComboBox.setItems(timeOptions);
            }

            if (dayComboBox != null) {
                ObservableList<String> days = FXCollections.observableArrayList(
                        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
                );
                dayComboBox.setItems(days);
            }

            if (terminalOSComboBox != null) {
                ObservableList<String> os = FXCollections.observableArrayList(
                        "macOS", "Windows"
                );
                terminalOSComboBox.setItems(os);
            }

            if (roomNumberComboBox != null) {
                ObservableList<String> room = FXCollections.observableArrayList(
                        "D524", "D526", "D426"
                );
                roomNumberComboBox.setItems(room);
            }

            if (statusComboBox != null) {
                ObservableList<String> status = FXCollections.observableArrayList(
                        "Active", "Down", "Maintenance" //TODO
                );
                statusComboBox.setItems(status);
            }
        });

        // Ensure table columns are initialized before setting cell value factories
        if (terminalColumn != null && roomNumberColumn != null && terminalOSColumn != null
                && dayColumn != null && timeColumn != null && statusColumn != null && addTerminalTableView != null) {

            terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
            roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
            terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOsProperty());
            dayColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
            statusColumn.setCellValueFactory(cellData -> cellData.getValue().terminalStatusProperty());

            // Load data from XML
            AddNewTerminalController.loadDataFromXML("src/main/java/server/util/terminal.xml");

            addTerminalTableView.setItems(terminalResults);

            // Refresh button action
            if (refreshButton != null) {
                refreshButton.setOnAction(event -> AddNewTerminalController.refreshTable());
            }
        }
    }

    public ComboBox<String> getDayComboBox() {
        return dayComboBox;
    }

    public void setDayComboBox(ComboBox<String> dayComboBox) {
        this.dayComboBox = dayComboBox;
    }

    public ComboBox<String> getTimeComboBox() {
        return timeComboBox;
    }

    public void setTimeComboBox(ComboBox<String> timeComboBox) {
        this.timeComboBox = timeComboBox;
    }
    public ComboBox<String> getRoomNumberComboBox() {
        return roomNumberComboBox;
    }
    public void setRoomNumberComboBox(ComboBox<String> roomNumberComboBox) {
        this.roomNumberComboBox = roomNumberComboBox;
    }
    public ComboBox<String> getTerminalOSComboBox() {
        return terminalOSComboBox;
    }
    public void setTerminalOSComboBox(ComboBox<String> terminalOSComboBox) {
        this.terminalOSComboBox = terminalOSComboBox;
    }
    public ComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }
    public void setStatusComboBox(ComboBox<String> statusComboBox) {
        this.statusComboBox = statusComboBox;
    }
}