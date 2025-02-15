package client.student.controller;

import client.student.view.ViewReservationView;
import client.student.model.ViewReservationModel;
import server.utility.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Map;

public class ViewReservationController {

    private ViewReservationView view;
    private ViewReservationModel model;

    public ViewReservationController(ViewReservationView view) {
        this.view = view;
        this.model = new ViewReservationModel();  // Initialize model
    }

    // Load reservations from the model and pass them to the view

}
