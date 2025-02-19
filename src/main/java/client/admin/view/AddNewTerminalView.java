package client.admin.view;

import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;
import server.utility.Terminal;

public class AddNewTerminalView {
    public TextField dateTextField;
    private AddNewTerminalController controller;

    @FXML
    private Button saveChangesButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchTerminalTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button redirectAddTerminalWindowButton;
    @FXML
    private TextField terminalNoTextField;
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
    private String startTime;
    private String endTime;

    // Getters for UI fields
    public TextField getTerminalNoTextField() {
        return terminalNoTextField;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
    public TextField getDateTextField(){
        return dateTextField;
    }

    public Button getSearchButton() {
        return searchButton;
    }
    public String getTerminalId() {
        return terminalId;
    }
    public void setStartTime(String startTime){
        this.startTime = startTime;
    }
    public void setEndTime(String endTime){
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
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
    private TableColumn<Terminal, String> dateColumn;
    @FXML
    private TableColumn<Terminal, String> startTimeColumn;
    @FXML
    private TableColumn<Terminal, String> endTimeColumn;
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
    public TextField getSearchTerminalTextField() {
        return searchTerminalTextField;
    }
    public TableView getAddTerminalTableView() {
        return addTerminalTableView;
    }

    public void setController(AddNewTerminalController controller) {
        this.controller = controller;
        System.out.println("[DEBUG] Controller has been set in AddNewTerminalView.");
    }

    @FXML
    public void initialize() {
        setupRedirectButtonAction();
        setupSaveChangesButtonAction();
        if (redirectAddTerminalWindowButton != null) {
            redirectAddTerminalWindowButton.setOnAction(event -> {
                // Ensure this works by checking the correct action
                AddNewTerminalController.redirectAddTerminalWindow(event);
            });
        }
        Platform.runLater(() -> {
            if (timeComboBox != null) {
                ObservableList<String> timeOptions = FXCollections.observableArrayList(
                        "09:30-11:30",
                        "11:30-16:30",
                        "07:30-15:30"
                );
                timeComboBox.setItems(timeOptions);
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
                        "Active", "Down", "Under Maintenance"
                );
                statusComboBox.setItems(status);
            }
        });

        // Ensure table columns are initialized before setting cell value factories
        if (terminalColumn != null && roomNumberColumn != null && terminalOSColumn != null
                && dateColumn != null && startTimeColumn != null && endTimeColumn != null && statusColumn != null && addTerminalTableView != null) {

            terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
            roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
            terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOsProperty());
          startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
            endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
            statusColumn.setCellValueFactory(cellData -> cellData.getValue().terminalStatusProperty());

            // Load data from XML
            AddNewTerminalController.loadDataFromXML("src/main/java/server/util/terminal.xml");

            addTerminalTableView.setItems(terminalResults);

            // Refresh button action
            if (refreshButton != null) {
                refreshButton.setOnAction(event -> AddNewTerminalController.refreshTable());
            }
            setupSearchFunctionality();
        }
        setupRefreshButtonAction();
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
    private void setupSearchFunctionality() {

        searchButton.setOnAction(event -> {
            String searchText = searchTerminalTextField.getText().toLowerCase();

            // If search is empty, show all terminals
            if (searchText.isEmpty()) {
                addTerminalTableView.setItems(terminalResults);
                return;
            }

            // Filter the list based on search text
            ObservableList<Terminal> filteredList = FXCollections.observableArrayList();
            for (Terminal terminal : terminalResults) {
                if (terminal.getTerminalId().toLowerCase().contains(searchText) ||
                        terminal.getTerminalRoom().toLowerCase().contains(searchText) ||
                        terminal.getTerminalOs().toLowerCase().contains(searchText) ||
                      terminal.getStartTime().toLowerCase().contains(searchText) ||
                        terminal.getEndTime().toLowerCase().contains(searchText) ||
                        terminal.getTerminalStatus().toLowerCase().contains(searchText)) {
                    filteredList.add(terminal);
                }
            }
            addTerminalTableView.setItems(filteredList);
        });
    }
    private void setupRedirectButtonAction() {
        if (redirectAddTerminalWindowButton != null) {
            redirectAddTerminalWindowButton.setOnAction(event -> {
                System.out.println("[DEBUG] Redirecting to Add Terminal window.");
                AddNewTerminalController.redirectAddTerminalWindow(event);
            });
        }
    }

    private void setupSaveChangesButtonAction() {
        if (saveChangesButton != null) {
            saveChangesButton.setOnAction(event -> {
                System.out.println("[DEBUG] Save changes action triggered.");
                // Handle save action here
            });
        }
    }

    private void setupRefreshButtonAction() {
        if (refreshButton != null) {
            refreshButton.setOnAction(event -> {
                System.out.println("[DEBUG] Refresh action triggered.");
                searchTerminalTextField.clear();
                AddNewTerminalController.loadDataFromXML("src/main/java/server/util/terminal.xml");
                addTerminalTableView.setItems(terminalResults); // Restore full list
            });
        }
    }

    public void saveChangesButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), saveChangesButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void saveChangesButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), saveChangesButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void searchButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), searchButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void searchButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), searchButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void addTerminalButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), redirectAddTerminalWindowButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void addTerminalButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), redirectAddTerminalWindowButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

}