package client.student.controller;

import client.student.view.ViewReservationView;
import client.student.model.ViewReservationModel;
import javafx.event.ActionEvent;
import server.utility.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewReservationController {
    private final ViewReservationView view;
    private final ViewReservationModel model;
    private final ObservableList<Reservation> allReservations = FXCollections.observableArrayList();
    public ViewReservationController(ViewReservationView view) {
        this.view = view;
        this.model = new ViewReservationModel();

        loadReservations(); // Load reservations into the table
        initializeButton();
    }

    public void initializeButton(){
        if (this.view.getSearchButton() != null) {
            this.view.getSearchButton().setOnAction(this::handleSearchButtonAction);
        } else {
            System.out.println("ERROR: Search button is NULL in Controller!");
        }

        // Set up the Refresh Button
        if (this.view.getRefreshButton() != null) {
            this.view.getRefreshButton().setOnAction(this::handleRefreshButtonAction);
            System.out.println("Refresh button action correctly set in Controller!");
        } else {
            System.out.println("ERROR: Refresh button is NULL in Controller!");
        }
    }

    private void loadReservations() {
        List<Reservation> reservations = model.fetchReservations();
        allReservations.setAll(reservations);  //Set reservations into the ObservableList
        view.getViewResTableView().setItems(allReservations); //Show reservations in the table
    }

    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.getViewResTableView().setItems(allReservations);
            return;
        }

        List<Reservation> filteredList = allReservations.stream()
                .filter(reservation -> {
                    String combinedFields = (reservation.getReservationId() + " " +
                            reservation.getReservationId() + " " +
                            reservation.getTerminalNumber() + " " +
                            reservation.getRoomNumber() + " " +
                            reservation.getDate() + " " +
                            reservation.getStartTime() + " " +
                            reservation.getEndTime() + " " +
                            reservation.getStatus()).toLowerCase();
                    return combinedFields.contains(searchQuery.toLowerCase());
                })
                .collect(Collectors.toList());

        view.getViewResTableView().setItems(FXCollections.observableArrayList(filteredList));
        view.getViewResTableView().refresh();//Force UI refresh
    }

    private void handleSearchButtonAction(javafx.event.ActionEvent actionEvent) {
        System.out.println("Search button clicked!");//Debugging

        if (view.getSearchField() != null) {
            String searchQuery = view.getSearchField().getText().trim();
            System.out.println("Search Query Entered: " + searchQuery);//Debugging
            filterReservations(searchQuery);
        } else {
            System.out.println("ERROR: Search field is NULL when button clicked!");
        }
    }
    private void handleRefreshButtonAction(ActionEvent event) {
        System.out.println("Refresh button clicked! Reloading reservations...");
        loadReservations(); // Reload data from XML
        view.getViewResTableView().refresh(); // Force UI refresh
        System.out.println("Reservations reloaded successfully!");
    }
    public static List<Reservation> parseReservedXML() {
        return ViewReservationModel.parseXML();
    }
}