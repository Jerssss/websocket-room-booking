package client.admin.controller;

import client.admin.model.ViewStudentReservationsModel;
import client.admin.view.ViewStudentReservationsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import server.utility.StudentReservation;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.stream.Collectors;

public class ViewStudentReservationsController {

    private ViewStudentReservationsView view;
    private ViewStudentReservationsModel model;
    private ObservableList<StudentReservation> allReservations = FXCollections.observableArrayList();

    public ViewStudentReservationsController(ViewStudentReservationsView view) {
        this.view = view;
        this.model = new ViewStudentReservationsModel();

        loadReservations(); // Load all reservations
        setupSearchFunctionality(); // <-- Call this method to set up search!

        this.view.setActionSearchButton(event -> { handleSearchButtonAction();
        });
    }

    /** Load reservations from XML into the TableView */
    private void loadReservations() {
        List<StudentReservation> reservations = model.loadStudentReservations(); // Fetch from model
        allReservations.setAll(reservations);
        view.getStudResTableView().setItems(allReservations);
    }

    /** Search functionality based on Room No */
    private void setupSearchFunctionality() {
        view.setActionSearchButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchQuery = view.getSearchStudResTextField().getText().trim();
                filterReservations(searchQuery);
            }
        });
    }

    /** Filter reservations based on search input */
    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.getStudResTableView().setItems(allReservations); // Show all if empty
            return;
        }

        List<StudentReservation> filteredList = allReservations.stream()
                .filter(reservation -> reservation.getTerminalRoom() != null &&
                        reservation.getTerminalRoom().equalsIgnoreCase(searchQuery))
                .collect(Collectors.toList());

        view.getStudResTableView().setItems(FXCollections.observableArrayList(filteredList));
    }
    public void handleSearchButtonAction() {
        view.setActionSearchButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchQuery = view.getSearchStudResTextField().getText().trim();

                // Check if the input is one of the allowed room numbers
                if (searchQuery.equalsIgnoreCase("D526") ||
                        searchQuery.equalsIgnoreCase("D524") ||
                        searchQuery.equalsIgnoreCase("D426")) {

                    List<StudentReservation> filteredList = allReservations.stream()
                            .filter(reservation -> searchQuery.equalsIgnoreCase(reservation.getTerminalRoom()))
                            .collect(Collectors.toList());

                    view.getStudResTableView().setItems(FXCollections.observableArrayList(filteredList));
                } else {
                    // If search is empty or not a valid room, reset to all reservations
                    view.getStudResTableView().setItems(allReservations);
                }
            }
        });
    }

}
