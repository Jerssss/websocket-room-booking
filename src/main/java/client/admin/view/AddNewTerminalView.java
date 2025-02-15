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
import server.admin.AddNewTerminalProcessor;
import server.utility.TerminalVer2;

import javax.swing.*;
import java.util.List;

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
    private TableView<TerminalVer2> addTerminalTableView;
    @FXML
    private TableColumn<TerminalVer2, String> terminalColumn;
    @FXML
    private TableColumn<TerminalVer2, String> roomNumberColumn;
    @FXML
    private TableColumn<TerminalVer2, String> terminalOSColumn;
    @FXML
    private TableColumn<TerminalVer2, String> dayColumn;
    @FXML
    private TableColumn<TerminalVer2, String> timeColumn;
    @FXML
    private TableColumn<TerminalVer2, String> statusColumn;
    @FXML
    public static ObservableList<TerminalVer2> terminalResults = FXCollections.observableArrayList();

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
        });

        // Ensure table columns are initialized before setting cell value factories
        if (terminalColumn != null && roomNumberColumn != null && terminalOSColumn != null
                && dayColumn != null && statusColumn != null && addTerminalTableView != null) {

            terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
            roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
            terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOSProperty());
            dayColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
            statusColumn.setCellValueFactory(cellData -> cellData.getValue().terminalStatusProperty());

            // Load data only when the TableView exists
            AddNewTerminalController.loadDataFromXML("src/main/java/server/util/terminal.xml");

            // Bind the ObservableList to the TableView
            addTerminalTableView.setItems(terminalResults);
            refreshButton.setOnAction(event -> AddNewTerminalController.refreshTable());
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
}