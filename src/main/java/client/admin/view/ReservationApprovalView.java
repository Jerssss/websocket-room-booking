package client.admin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import server.utility.StudentReservation;

import java.util.List;

public class ReservationApprovalView {

<<<<<<< HEAD
    @FXML private TextField searchStudResTextField;
    @FXML private Button searchButton;
    @FXML private TableView<StudentReservation> approveResTableView;
    @FXML private TableColumn<StudentReservation, String> resIDColumn;
    @FXML private TableColumn<StudentReservation, String> userIDColumn;
    @FXML private TableColumn<StudentReservation, String> terminalColumn;
    @FXML private TableColumn<StudentReservation, String> roomNumberColumn;
    @FXML private TableColumn<StudentReservation, String> dateColumn;
    @FXML private TableColumn<StudentReservation, String> statusColumn;
=======
    @FXML
    private TableView<StudentReservation> approveResTableView;
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
>>>>>>> d093ef69fd2b41ec74f17413e741f6dc7c04beda

    @FXML
    private TextField searchStudResTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button refreshButton;

    private final ObservableList<StudentReservation> reservationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        resIDColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        terminalColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("terminalRoom"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("terminalStatus"));

        approveResTableView.setItems(reservationList);
    }


    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
    }

 
    public void displayApprovalReservations(ObservableList<StudentReservation> reservations) {
        approveResTableView.setItems(reservations);
    }
    
    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }
    
    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }

<<<<<<< HEAD
    // Set up table columns
    public void initializeTable() {
        TableUtils.setupColumn(resIDColumn, "reservationId");
//        TODO: TableUtils.setupColumn(userIDColumn, "xx");
        TableUtils.setupColumn(terminalColumn, "terminalId");
        TableUtils.setupColumn(roomNumberColumn, "terminalRoom");
        TableUtils.setupColumn(dateColumn, "date");
        TableUtils.setupColumn(statusColumn, "terminalStatus");
=======
    public void displayApprovalReservations(List<StudentReservation> filteredList) {
>>>>>>> d093ef69fd2b41ec74f17413e741f6dc7c04beda
    }
}
