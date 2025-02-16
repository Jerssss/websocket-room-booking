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
    public Label studResTitleLabel;
    public Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<StudentReservation> studResTableView;
    @FXML
    private TableColumn<StudentReservation, String> resIDColumn;
    @FXML
    private TableColumn<StudentReservation, String> terminalColumn;
    @FXML
    private TableColumn<StudentReservation, String> roomNumberColumn;
    @FXML
    private TableColumn<StudentReservation, String> dateColumn;
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
        resIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReservationId()));
        terminalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalId()));
        roomNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalRoom()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
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
