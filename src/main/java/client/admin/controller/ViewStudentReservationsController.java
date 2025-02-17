package client.admin.controller;
import client.admin.model.ViewStudentReservationsModel;
import client.admin.view.ViewStudentReservationsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.StudentReservation;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;


public class ViewStudentReservationsController {

    private final ViewStudentReservationsView view;
    private final ViewStudentReservationsModel model;
    private final ObservableList<StudentReservation> allReservations = FXCollections.observableArrayList();
    public ViewStudentReservationsController(ViewStudentReservationsView view) {
        this.view = view;
        this.model = new ViewStudentReservationsModel();

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
        List<StudentReservation> reservations = model.fetchStudentReservations();
        allReservations.setAll(reservations);  //Set reservations into the ObservableList
        view.getStudResTableView().setItems(allReservations); //Show reservations in the table
    }

    private void filterReservations(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.getStudResTableView().setItems(allReservations);
            return;
        }

        List<StudentReservation> filteredList = allReservations.stream()
                .filter(reservation -> {
                    String combinedFields = (reservation.getReservationId() + " " +
                            reservation.getReservationId() + " " +
                            reservation.getUserId() + " " +
                            reservation.getTerminalRoom() + " " +
                            reservation.getDate() + " " +
                            reservation.getStartTime() + " " +
                            reservation.getEndTime() + " " +
                            reservation.getTerminalStatus()).toLowerCase();
                    return combinedFields.contains(searchQuery.toLowerCase());
                })
                .collect(Collectors.toList());

        view.getStudResTableView().setItems(FXCollections.observableArrayList(filteredList));
        view.getStudResTableView().refresh();//Force UI refresh
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
        view.getStudResTableView().refresh(); // Force UI refresh
        System.out.println("Reservations reloaded successfully!");
    }
    public static List<StudentReservation> parseReservedXML() {
        return ViewStudentReservationsModel.parseXML();
    }
}
