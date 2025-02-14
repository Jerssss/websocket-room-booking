package client.student.view;

import client.student.model.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import client.student.controller.ViewReservationController;

public class ViewReservationView {

    @FXML
    private TableView<Reservation> modResTableView;

    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;

    @FXML
    private TableColumn<Reservation, String> userIDColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    private ViewReservationController controller;

    @FXML
    public void initialize() {
        // Initialize columns
        reservationIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIDProperty());
        userIDColumn.setCellValueFactory(cellData -> cellData.getValue().userIDProperty());

        // Initialize controller
        controller = new ViewReservationController(this);

        // Load reservations
        controller.loadReservations();
    }

    // Method to set data in the TableView from the controller
    public void setReservationData(ObservableList<Reservation> data) {
        System.out.println("Number of reservations: " + reservationData.size());

        reservationData.setAll(data);
        modResTableView.setItems(reservationData);

    }
}
