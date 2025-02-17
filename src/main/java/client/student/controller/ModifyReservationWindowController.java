package client.student.controller;

import client.student.view.ModifyReservationWindowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.Reservation;

import java.util.List;

public class ModifyReservationWindowController {

    private ModifyReservationWindowView view;
    private ModifyReservationWindowModel model;
    private ObservableList<Reservation> allReservations = FXCollections.observableArrayList();

    public ModifyReservationWindowController(ModifyReservationWindowView view){
        this.view = view;
        this.model = new ModifyReservationWindowModel();

        loadReservations();

    }


    private void loadReservations() {
        List<Reservation> reservations = model.fetchAllReservations(); // Fetch from model
        allReservations.setAll(reservations);
        view.getStudResTableView().setItems(allReservations);
    }
}
