package client.admin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import client.admin.controller.ModifyTerminalStatusController;
import server.utility.Terminal;

public class ModifyTerminalStatusView {

    @FXML
    private Button searchButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    private TextField searchStudResTextField;
    @FXML
    private TableView<Terminal> modResTableView;
    @FXML
    private TableColumn<Terminal, String> roomNumberColumn;
    @FXML
    private TableColumn<Terminal, String> terminalColumn;
    @FXML
    private TableColumn<Terminal, String> terminalOSColumn;
    @FXML
    private TableColumn<Terminal, String> terminalStatusColumn;

    private ModifyTerminalStatusController controller;
    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();

    public void initialize() {
        controller = new ModifyTerminalStatusController(this);

        // Set up columns with data
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
        terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOsProperty());

        // Terminal status with dropdown
        terminalStatusColumn.setCellValueFactory(cellData -> cellData.getValue().terminalStatusProperty());
        terminalStatusColumn.setCellFactory(createStyledStatusCellFactory());

        // Load data via controller
        controller.loadTerminalData();

        // Set event handler for save button
        setActionSaveChangesButton(event -> controller.saveChanges());
    }

    public void setTerminalData(ObservableList<Terminal> data) {
        terminalData.setAll(data);
        modResTableView.setItems(terminalData);
    }

    private Callback<TableColumn<Terminal, String>, TableCell<Terminal, String>> createStyledStatusCellFactory() {
        return column -> new TableCell<Terminal, String>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>();

            {
                statusComboBox.getItems().addAll("Active", "Reserved", "Under Maintenance");
                statusComboBox.setStyle("-fx-border-color: transparent; -fx-padding: 5px; " +
                        "-fx-font-size: 14px; -fx-font-family: 'Arial';");

                statusComboBox.setOnAction(e -> {
                    Terminal terminal = getTableRow().getItem();
                    if (terminal != null) {
                        terminal.setTerminalStatus(statusComboBox.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Terminal terminal = getTableRow().getItem();
                    statusComboBox.setValue(terminal.getTerminalStatus());

                    // Apply alternate row colors
                    int rowIndex = getIndex();
                    Color rowColor = (rowIndex % 2 == 1) ? Color.web("#f8f8f8") : Color.WHITE;
                    setBackground(new Background(new BackgroundFill(rowColor, new CornerRadii(5), null)));

                    statusComboBox.setStyle("-fx-background-color: " + toRGBCode(rowColor) + "; " +
                            "-fx-border-color: transparent; -fx-padding: 5px; " +
                            "-fx-font-size: 14px; -fx-font-family: 'Arial';");

                    statusComboBox.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(statusComboBox);
                }
            }

            private String toRGBCode(Color color) {
                return String.format("#%02X%02X%02X",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
            }
        };
    }

    public void setActionSaveChangesButton(EventHandler<ActionEvent> event) {
        saveChangesButton.setOnAction(event);
    }

    public ObservableList<Terminal> getTerminalData() {
        return terminalData;
    }
}
