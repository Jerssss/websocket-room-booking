package client.student.controller;

import client.student.view.ViewReservationView;
import client.student.model.ViewReservationModel;
import client.student.model.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Map;

public class ViewReservationController {

    private ViewReservationView view;
    private ViewReservationModel model;

    public ViewReservationController(ViewReservationView view) {
        this.view = view;
        try {
            this.model = new ViewReservationModel();  // Initialize model
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load reservations from the model and pass them to the view
    public void loadReservations() {
        Map<String, Map<String, String>> reservationsData = model.fetchAllReservations();

        ObservableList<Reservation> reservations = FXCollections.observableArrayList();
        for (Map<String, String> reservationMap : reservationsData.values()) {
            reservations.add(new Reservation(
                    reservationMap.get("reservation_id"),
                    reservationMap.get("Student_ID"),
                    reservationMap.get("terminal_id"),
                    reservationMap.get("terminal_room"),
                    reservationMap.get("terminal_status")
            ));
        }

        // Pass the data to the view
        view.setReservationData(reservations);
    }
}
