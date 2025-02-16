package client.admin.controller;


import client.admin.model.ViewStudentReservationsModel;
import client.admin.view.ViewStudentReservationsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.StudentReservation;


import java.util.List;
import java.util.stream.Collectors;


public class ViewStudentReservationsController {


    private ViewStudentReservationsView view;
    private ViewStudentReservationsModel model;
    private ObservableList<StudentReservation> allReservations = FXCollections.observableArrayList();


    public ViewStudentReservationsController(ViewStudentReservationsView view) {
        this.view = view;
        this.model = new ViewStudentReservationsModel();


        loadReservations();
        setupSearchFunctionality();


        this.view.setActionSearchButton(event -> handleSearchButtonAction());
    }


    private void loadReservations() {
        List<StudentReservation> reservations = model.fetchStudentReservations();
        allReservations.setAll(reservations);
        view.getStudResTableView().setItems(allReservations);
    }


    private void setupSearchFunctionality() {
        view.setActionSearchButton(event -> {
            String searchQuery = view.getSearchStudResTextField().getText().trim();
            filterReservations(searchQuery);
        });
    }


    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.getStudResTableView().setItems(allReservations);
            return;
        }


        List<StudentReservation> filteredList = allReservations.stream()
                .filter(reservation -> reservation.getTerminalRoom() != null &&
                        reservation.getTerminalRoom().equalsIgnoreCase(searchQuery))
                .collect(Collectors.toList());


        view.getStudResTableView().setItems(FXCollections.observableArrayList(filteredList));
    }


    private void handleSearchButtonAction() {
        String searchQuery = view.getSearchStudResTextField().getText().trim();


        if (searchQuery.equalsIgnoreCase("D526") ||
                searchQuery.equalsIgnoreCase("D524") ||
                searchQuery.equalsIgnoreCase("D426")) {


            List<StudentReservation> filteredList = allReservations.stream()
                    .filter(reservation -> searchQuery.equalsIgnoreCase(reservation.getTerminalRoom()))
                    .collect(Collectors.toList());


            view.getStudResTableView().setItems(FXCollections.observableArrayList(filteredList));
        } else {
            view.getStudResTableView().setItems(allReservations);
        }
    }
    public static List<StudentReservation> parseReservedXML() {
        return ViewStudentReservationsModel.parseXML();
    }
}
