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
import server.admin.ModifyTerminalProcessor;
import server.utility.Terminal;
import java.util.List;

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

    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();

    public void initialize() {
        // Set up columns with data
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
        terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOsProperty());

        // For the terminalStatusColumn, create a styled ComboBox cell
        terminalStatusColumn.setCellValueFactory(cellData -> cellData.getValue().terminalStatusProperty());
        terminalStatusColumn.setCellFactory(createStyledStatusCellFactory());

        // Load data from XML
        loadDataFromXML("src/main/java/server/util/terminal.xml");

        // Set the table items
        modResTableView.setItems(terminalData);
    }

    private void loadDataFromXML(String filePath) {
        List<Terminal> terminal = ModifyTerminalProcessor.parseXML(filePath);
        if (terminal != null) {
            terminalData.addAll(terminal);
        }
    }

    private Callback<TableColumn<Terminal, String>, TableCell<Terminal, String>> createStyledStatusCellFactory() {
        return column -> new TableCell<Terminal, String>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>();

            {
                // Set ComboBox options
                statusComboBox.getItems().addAll("Active", "Reserved", "Under Maintenance");

                // Remove default border and add padding for better blending
                statusComboBox.setStyle("-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-family: 'Arial';");

                // Update the terminal status when selection is changed
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

                    // Even lighter gray for odd rows, pure white for even rows
                    int rowIndex = getIndex();
                    Color rowColor = (rowIndex % 2 == 1) ? Color.web("#EEEEEE") : Color.WHITE;

                    // Apply row background
                    setBackground(new Background(new BackgroundFill(rowColor, new CornerRadii(5), null)));

                    // Set ComboBox background to match row color
                    statusComboBox.setStyle("-fx-background-color: " + toRGBCode(rowColor) + "; " +
                            "-fx-border-color: transparent; " +
                            "-fx-padding: 5px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-family: 'Arial';");

                    statusComboBox.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(statusComboBox);
                }
            }

            // Convert Color to RGB hex code
            private String toRGBCode(Color color) {
                return String.format("#%02X%02X%02X",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
            }
        };
    }


    // Unused methods but retained for reference as requested
    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }

    public void setActionSaveChangesButton(EventHandler<ActionEvent> event) {
        saveChangesButton.setOnAction(event);
    }

    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }
}
