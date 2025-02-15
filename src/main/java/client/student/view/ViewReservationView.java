package client.student.view;

import server.utility.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.student.ViewReservationProcessor;

import java.util.List;

public class ViewReservationView {

    @FXML
    private TableView<Reservation> modResTableView;

    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;

    @FXML
    private TableColumn<Reservation, String> userIDColumn;

    @FXML
    private TableColumn<Reservation, String> terminalIDColumn;

    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;

    @FXML
    private TableColumn<Reservation, String> startTimeColumn;

    @FXML
    private TableColumn<Reservation, String> endTimeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    public TableView<Reservation> getStudResTableView() {
        return modResTableView;  // Ensure studResTableView is properly initialized
    }
    @FXML
    public void initialize() {
        // Initialize columns and bind properties
        reservationIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        userIDColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        terminalIDColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().reservationStatusProperty());


        loadDataFromXML("src/main/java/server/util/reservationapproval.xml");

        modResTableView.setItems(reservationData);
    }
    private void loadDataFromXML(String filePath) {
        List<Reservation> reservations = ViewReservationProcessor.parseXML(filePath);
        if (reservations != null) {
            reservationData.addAll(reservations);
        }
    }

}

