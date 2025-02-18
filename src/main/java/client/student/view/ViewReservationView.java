package client.student.view;

import client.student.controller.ViewReservationController;
import client.student.model.ViewReservationModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import server.utility.Reservation;
import javafx.fxml.FXML;
import java.util.List;

public class ViewReservationView {

    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Reservation> viewResTableView;
    @FXML
    private TextField searchResTextField;
    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;
    @FXML
    private TableColumn<Reservation, String> terminalNumberColumn;
    @FXML
    private TableColumn<Reservation, String> roomNumberColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> startTimeColumn;
    @FXML
    private TableColumn<Reservation, String> endTimeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;
    private String sessionToken;
    private ViewReservationModel model;

    public Button getSearchButton() {return searchButton;}
    public Button getRefreshButton() {return refreshButton;}

    public TextField getSearchField() {
        if (searchResTextField == null) {
            System.err.println("Error: Search field is NULL in View");
        }
        return searchResTextField;
    }

    public TableView<Reservation> getViewResTableView() {
        return viewResTableView;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        this.model = new ViewReservationModel(sessionToken);
        System.out.println("DEBUG: Session token set in View: " + sessionToken);
        initializeWithToken();
    }

    @FXML
    public void initialize() {
        System.out.println("ViewReservationsView initialized!");

        // Initialize columns with data
        reservationIDColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReservationId()));
        terminalNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTerminalNumber()));
        roomNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRoomNumber()));
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));
        startTimeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartTime()));
        endTimeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEndTime()));
        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        // Initialize the controller with the view and model
        new ViewReservationController(this, model);

        // Event handler for search button
        searchButton.setOnAction(e -> searchReservations());

        // Event handler for refresh button
        refreshButton.setOnAction(e -> refreshReservations());
    }


    public void searchReservations() {
        String searchText = searchResTextField.getText().toLowerCase();
        List<Reservation> filteredReservations = model.fetchReservations().stream()
                .filter(reservation ->
                        reservation.getReservationId().toLowerCase().contains(searchText) ||
                                reservation.getTerminalNumber().toLowerCase().contains(searchText) ||
                                reservation.getRoomNumber().toLowerCase().contains(searchText) ||
                                reservation.getDate().toLowerCase().contains(searchText) ||
                                reservation.getStartTime().toLowerCase().contains(searchText) ||
                                reservation.getEndTime().toLowerCase().contains(searchText) ||
                                reservation.getStatus().toLowerCase().contains(searchText)
                )
                .toList();

        ObservableList<Reservation> filteredData = FXCollections.observableArrayList(filteredReservations);
        viewResTableView.setItems(filteredData);
    }

    private void refreshReservations() {
        showReservationsInTable();
    }

    public void initializeWithToken() {
        if (sessionToken == null) {
            System.err.println("Error: Session token is NULL in View");
            return;
        }
        if (model == null) {
            System.err.println("Error: Model is null!");
            return;
        }
        showReservationsInTable();
    }

    private void showReservationsInTable() {
        if (model == null) {
            System.err.println("Error: Model is null!");
            return;
        }
        List<Reservation> reservations = model.fetchReservations();
        ObservableList<Reservation> reservationData = FXCollections.observableArrayList(reservations);
        viewResTableView.setItems(reservationData);
    }
    public void searchButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), searchButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void searchButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), searchButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}