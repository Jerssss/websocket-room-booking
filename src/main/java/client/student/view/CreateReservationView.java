package client.student.view;

import client.admin.controller.AddNewTerminalController;
import client.admin.controller.ModifyTerminalStatusController;
import client.admin.view.AddNewTerminalView;
import client.student.controller.CreateReservationController;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import server.admin.AddNewTerminalProcessor;
import server.student.CreateReservationProcessor;
import server.utility.Terminal;

import java.util.List;

public class CreateReservationView {

    @FXML
    private VBox centerPane;

    @FXML
    private TableView<Terminal> createReservationTableView;

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
    private Button redirectCreateReservationWindowButton;

    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> timeComboBox;
    @FXML
    private ComboBox<String> terminalOSComboBox;
    @FXML
    private ComboBox<String> roomNumberComboBox;

    @FXML
    private Button saveChangesButton;

    @FXML
    private TextField terminalNoTextField;

    private String terminalId;
    private String room;
    private String osType;


    private CreateReservationController controller = new CreateReservationController(this);
    public static ObservableList<Terminal> reservationData = FXCollections.observableArrayList();

    public void setController (CreateReservationController controller) {this.controller = controller;}

    public void initialize() {
        if (redirectCreateReservationWindowButton != null) {
            redirectCreateReservationWindowButton.setOnAction(CreateReservationController::redirectCreateReservationWindow);
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
        });

        // Ensure table columns are initialized before setting cell value factories
        if (terminalColumn != null && roomNumberColumn != null && terminalOSColumn != null
                && dayColumn != null && timeColumn != null && createReservationTableView != null) {

            terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
            roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
            terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOsProperty());
            dayColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

            // Load data from XML
            CreateReservationController.loadDataFromXML("src/main/java/server/util/terminal.xml");

            createReservationTableView.setItems(reservationData);

            // Refresh button action
            if (refreshButton != null) {
                refreshButton.setOnAction(event -> CreateReservationController.refreshTable());
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


    public void setTerminalData(ObservableList<Terminal> data) {
        reservationData.setAll(data); // Update dataset
        createReservationTableView.setItems(null); // Force reset
        createReservationTableView.setItems(reservationData); // Reload table data
        createReservationTableView.refresh(); // Force UI refresh
        System.out.println("[DEBUG] Terminal data updated. New table size: " + reservationData.size());
    }

    // Getters for the components
    public VBox getCenterPane() {
        return centerPane;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public TextField getTerminalNoTextField() {
        return terminalNoTextField;
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

    // Setters and getters for button actions
    public void setSaveChangesButtonAction(EventHandler<ActionEvent> handler) {
   //     saveChangesButton.setOnAction(handler);
    }
    public Button getSaveChangesButton() {
        return saveChangesButton;
    }

    // Method to load data from the XML file
    public static void loadDataFromXML(String filePath) {
        List<Terminal> reservation = CreateReservationProcessor.parseXML(filePath);
        if (reservation != null) {
            CreateReservationView.reservationData.clear(); // Clear the current data
            CreateReservationView.reservationData.addAll(reservation);
        }
    }

    public void createButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), redirectCreateReservationWindowButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void createButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), redirectCreateReservationWindowButton);
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
}
