package client.student.view;

import client.admin.controller.AddNewTerminalController;
import client.admin.controller.ModifyTerminalStatusController;
import client.student.controller.CreateReservationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import server.utility.Terminal;

public class CreateReservationView {

    @FXML
    private VBox centerPane;

    @FXML
    private Label studResTitleLabel;

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
    private Button redirectCreateReservationWindowButton;

    @FXML
    private Button refreshButton;


    private CreateReservationController controller = new CreateReservationController(this);
    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();


    public void initialize(){
        terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        terminalOSColumn.setCellValueFactory(cellData -> cellData.getValue().terminalOsProperty());
        dayColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        if (controller != null) {
            controller.loadTerminalData();
        }

        if (redirectCreateReservationWindowButton != null) {
            redirectCreateReservationWindowButton.setOnAction(CreateReservationController::redirectCreateReservationWindow);
        }




    }


    public void setTerminalData(ObservableList<Terminal> data) {
        terminalData.setAll(data); // Update dataset
        addTerminalTableView.setItems(null); // Force reset
        addTerminalTableView.setItems(terminalData); // Reload table data
        addTerminalTableView.refresh(); // Force UI refresh
        System.out.println("[DEBUG] Terminal data updated. New table size: " + terminalData.size());
    }



    // Getters for the components
    public VBox getCenterPane() {
        return centerPane;
    }


    public Button getRefreshButton() {
        return refreshButton;
    }





}
