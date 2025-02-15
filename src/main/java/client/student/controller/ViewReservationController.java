package client.student.controller;

import client.student.view.ViewReservationView;
import client.student.model.ViewReservationModel;
import server.utility.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ViewReservationController {

    private ViewReservationView view;
    private ViewReservationModel model;
    private ObservableList<Reservation> allReservations = FXCollections.observableArrayList();

    public ViewReservationController(ViewReservationView view){
        this.view = view;
        this.model = new ViewReservationModel();

        loadReservations();

    }





    private void loadReservations() {
        List<Reservation> reservations = model.fetchAllReservations(); // Fetch from model
        allReservations.setAll(reservations);
        view.getStudResTableView().setItems(allReservations);
    }

}
