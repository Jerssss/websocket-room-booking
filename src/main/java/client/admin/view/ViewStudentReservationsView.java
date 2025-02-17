package client.admin.view;

import client.admin.controller.ViewStudentReservationsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.admin.ViewStudentReservationsProcessor;
import server.utility.StudentReservation;
import java.util.List;

public class ViewStudentReservationsView {
    @FXML
    public Label studResTitleLabel;
    @FXML
    public Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<StudentReservation> studResTableView;
    @FXML
    private TableColumn<StudentReservation, String> reservationIdColumn;
    @FXML
    private TableColumn<StudentReservation, String> userIdColumn;

    @FXML
    private TableColumn<StudentReservation, String> terminalNumberColumn;
    @FXML
    private TableColumn<StudentReservation, String> roomNumberColumn;
    @FXML
    private TableColumn<StudentReservation, String> dateColumn;
    @FXML
    private TableColumn<StudentReservation, String> startTimeColumn;
    @FXML
    private TableColumn<StudentReservation, String> endTimeColumn;
    @FXML
    private TableColumn<StudentReservation, String> statusColumn;

    public Button getSearchButton() {
        return searchButton;
    }
    public Button getRefreshButton() {
        return refreshButton;
    }
    public TextField getSearchField() {
        if (searchTextField == null) {
            System.out.println("ERROR: Search field is NULL in View!");
        }
        return searchTextField;
    }

    public TableView<StudentReservation> getStudResTableView() {
        return studResTableView;
    }

    @FXML
    public void initialize() {
        System.out.println("ViewStudentReservationsView initialized!"); // Debugging
        reservationIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReservationId()));
        userIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserId()));
        terminalNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalNumber()));
        roomNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalRoom()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalStatus()));

        System.out.println("ViewStudentReservationsView initialized!");

        if (searchButton != null) {
            System.out.println("Search button exists in FXML!");
        } else {
            System.out.println("ERROR: Search button is NULL!");
        }

        if (searchTextField != null) {
            System.out.println("Search field exists in FXML!");
        } else {
            System.out.println("ERROR: Search field is NULL!");
        }

        new ViewStudentReservationsController(this);

        showReservationsInTable();
    }

    private void showReservationsInTable() {
        List<StudentReservation> reservations = ViewStudentReservationsProcessor.loadStudentReservationsFromXML();
        ObservableList<StudentReservation> observableList = FXCollections.observableArrayList(reservations);
        studResTableView.setItems(observableList);
    }
}
