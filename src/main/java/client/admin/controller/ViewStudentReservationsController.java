package client.admin.controller;

import client.admin.view.ViewStudentReservationsView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ViewStudentReservationsController {

    private ViewStudentReservationsView view;

    // Constructor to set the view (in case you need it to access view methods)
    public ViewStudentReservationsController(ViewStudentReservationsView view) {
        this.view = view;
    }

    // Set the action for the Search button
    public void handleSearchButtonAction() {
        view.setActionSearchButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Add the logic for search button click
                System.out.println("Search button clicked!");
            }
        });
    }
}