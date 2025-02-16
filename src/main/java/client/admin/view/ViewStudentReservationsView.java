package client.admin.view;


import client.admin.controller.ViewStudentReservationsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.admin.ViewStudentReservationsProcessor;
import server.utility.StudentReservation;


import java.util.List;


public class ViewStudentReservationsView {


    @FXML
    private Button searchButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchStudResTextField;
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


    private final ObservableList<StudentReservation> studResData = FXCollections.observableArrayList();


    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }


    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }


    public TableView<StudentReservation> getStudResTableView() {
        return studResTableView;
    }


    @FXML
    public void initialize() {
        resIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReservationId()));
        terminalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalId()));
        roomNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalRoom()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalStatus()));

        showReservationsInTable();
    }

    private void showReservationsInTable() {
        List<StudentReservation> reservations = ViewStudentReservationsController.parseReservedXML();
        ObservableList<StudentReservation> observableList = FXCollections.observableArrayList(reservations);
        studResTableView.setItems(observableList);
    }
}