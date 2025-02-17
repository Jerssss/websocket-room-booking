package client.student.view;

import client.student.controller.ViewReservationController;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.student.ViewReservationProcessor;
import server.utility.Reservation;
import javafx.fxml.FXML;
import java.util.List;

public class ViewReservationView {

    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Reservation> viewResTableView;
    @FXML
    private TextField searchResTextField;
    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;
    @FXML
    private TableColumn<Reservation, String> terminalNumberColumn;
    @FXML
    private TableColumn<Reservation, String> roomNumberColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> startTimeColumn;
    @FXML
    private TableColumn<Reservation, String> endTimeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    public Button getSearchButton() {return searchButton;}
    public Button getRefreshButton() {return refreshButton;}
    public TextField getSearchField(){
        if (searchResTextField == null) {
            System.err.println("Error: Search field is NULL in View");
        }
        return searchResTextField;
    }
    public TableView<Reservation> getViewResTableView() {
        return viewResTableView;
    }

    @FXML
    public void initialize() {
        System.out.println("ViewReservationsView initialized!"); // Debugging
        reservationIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReservationId()));
        terminalNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerminalNumber()));
        roomNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomNumber()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        if (searchButton != null) {
            System.out.println("Search button exists in FXML!");
        } else {
            System.out.println("ERROR: Search button is NULL!");
        }

        if (searchResTextField != null) {
            System.out.println("Search field exists in FXML!");
        } else {
            System.out.println("ERROR: Search field is NULL!");
        }

        new ViewReservationController(this);

        showReservationsInTable();
    }

    private void showReservationsInTable() {
        List<Reservation> reservations = ViewReservationProcessor.loadReservationFromXML();
        ObservableList<Reservation> reservationData = FXCollections.observableArrayList(reservations);
        viewResTableView.setItems(reservationData);
    }
}