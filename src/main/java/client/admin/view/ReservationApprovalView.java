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

    public void displayApprovalReservations(List<StudentReservation> filteredList) {
    }
}
