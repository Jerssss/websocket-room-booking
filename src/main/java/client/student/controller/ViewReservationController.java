package client.student.controller;

import client.student.view.ViewReservationView;
import client.student.model.ViewReservationModel;
import server.utility.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class ViewReservationController {

    private ViewReservationView view;
    private ViewReservationModel model;
    private String sessionToken; // Store session token

    public ViewReservationController(ViewReservationView view, String sessionToken) {
        this.view = view;
        this.model = new ViewReservationModel();
        this.sessionToken = sessionToken; // Store the session token

        // Set session token in view
        this.view.setSessionToken(sessionToken);

        // Load reservations
        loadReservations();
    }

    // Load reservations from the model and update the view
    public void loadReservations() {
        if (sessionToken == null) {
            System.err.println("❌ Error: Session token is null in Controller!");
            return;
        }

        List<Reservation> reservations = model.fetchReservations(sessionToken);
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList(reservations);

        // Update the view with the loaded reservations
        view.getStudResTableView().setItems(reservationList);
        view.getStudResTableView().refresh();
    }
}
