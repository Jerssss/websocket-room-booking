// client/student/controller/ViewReservationController.java
package client.student.controller;

import client.student.model.ViewReservationModel;
import client.student.view.ViewReservationView;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import server.utility.Reservation;

import java.util.List;

public class ViewReservationController {
    private ViewReservationView view;
    private ViewReservationModel model;

        public ViewReservationController(ViewReservationView view, ViewReservationModel model) {
            this.view = view;
            this.model = model;
            initializeEventHandlers();
        }

        private void initializeEventHandlers() {
            // Assign event handlers for refresh and search buttons
            view.getRefreshButton().setOnAction(e -> refreshReservations());
            view.getSearchButton().setOnAction(e -> view.searchReservations());
        }

        private void refreshReservations() {
            List<Reservation> reservations = model.fetchReservations();
            view.getViewResTableView().setItems(FXCollections.observableArrayList(reservations));

        }
}
