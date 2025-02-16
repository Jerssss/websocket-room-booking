package client.student.view;

import client.utility.SessionManager;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.Reservation;
import javafx.fxml.FXML;
import server.student.ViewReservationProcessor;
import client.utility.ServerConnectionManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ViewReservationView {

    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<Reservation> modResTableView;

    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;
    @FXML
    private TableColumn<Reservation, String> userIDColumn;
    @FXML
    private TableColumn<Reservation, String> terminalIDColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, String> startTimeColumn;
    @FXML
    private TableColumn<Reservation, String> endTimeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();
    public TableView<Reservation> getStudResTableView() {
        return modResTableView;
    }
    private String sessionToken;
    @FXML
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void initialize() throws IOException {
        // Initialize columns and bind properties
        reservationIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        userIDColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        terminalIDColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().reservationStatusProperty());
        modResTableView.setItems(reservationData);

        // Populate ComboBoxes with default values
        populateComboBoxes();

        // Add event listeners for month and year ComboBoxes
        monthComboBox.setOnAction(event -> filterReservations());
        yearComboBox.setOnAction(event -> filterReservations());
    }

    // Populate ComboBoxes with months, days, and years, and set default values
    private void populateComboBoxes() {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue(); // Month as an integer (1-12)
        int currentDay = currentDate.getDayOfMonth(); // Day of the month (1-31)
        int currentYear = currentDate.getYear(); // Current year

        // Populate months (January to December) with a default "Select Month" option
        ObservableList<String> months = FXCollections.observableArrayList(
                "Select Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        monthComboBox.setItems(months);
        monthComboBox.getSelectionModel().select(0); // Default to "Select Month"

        // Populate days (1 to 31) with a default "Select Day" option
        ObservableList<String> days = FXCollections.observableArrayList();
        days.add("Select Day");
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        dayComboBox.setItems(days);
        dayComboBox.getSelectionModel().select(0); // Default to "Select Day"

        // Populate years (current year to 5 years ahead) with a default "Select Year" option
        ObservableList<String> years = FXCollections.observableArrayList();
        years.add("Select Year");
        for (int i = currentYear; i < currentYear + 6; i++) {
            years.add(String.valueOf(i));
        }
        yearComboBox.setItems(years);
        yearComboBox.getSelectionModel().select(0); // Default to "Select Year"
    }

    public void loadReservationData() throws IOException {
        if (sessionToken == null) {
            System.err.println("Error: Session token not set!");
            return;
        }
        loadDataForLoggedInUser(sessionToken); // Now uses the class field
    }

    // Load reservations for the logged-in user only
    private void loadDataForLoggedInUser(String sessionToken) throws IOException {
        String loggedInUserId = SessionManager.getUserId(sessionToken);  // Fetch logged-in user ID
        System.out.println("Logged-in User ID: " + loggedInUserId);  // Debugging logged-in user ID

        // Check if the user has an established connection
        if (loggedInUserId == null) {
            System.err.println("Error: Invalid or expired session token!");
            return;
        }

        // XML Path verification
        String xmlPath = "src/main/java/server/util/reservationapproval.xml";
        System.out.println("Debug - Loading XML from: " + new File(xmlPath).getAbsolutePath());

        // Load the reservations and filter them by logged-in user's ID
        List<Reservation> reservations = ViewReservationProcessor.parseXML("src/main/java/server/util/reservationapproval.xml");
        if (reservations != null) {
            for (Reservation reservation : reservations) {
                if (reservation.getUserId().equals(loggedInUserId)) {
                    reservationData.add(reservation);  // Add only the logged-in user's reservations
                }
            }
        }
    }

    // Filter reservations based on selected year and month
    private void filterReservations() {
        String selectedMonth = monthComboBox.getValue();
        String selectedYear = yearComboBox.getValue();

        // Clear the current reservation data
        reservationData.clear();

        String loggedInUserId = SessionManager.getUserId(sessionToken);
        if (loggedInUserId == null) {
            System.err.println("Error: Invalid session token!");
            return;
        }
        List<Reservation> reservations = ViewReservationProcessor.parseXML("src/main/java/server/util/reservationapproval.xml");

        if (reservations != null) {
            for (Reservation reservation : reservations) {
                LocalDate reservationDate = LocalDate.parse(reservation.getReservationDate()); // Assuming reservationDate is a LocalDate

                boolean matchesYear = !selectedYear.equals("Select Year") && String.valueOf(reservationDate.getYear()).equals(selectedYear);

                // If only the year is selected
                boolean matchesMonth = !selectedMonth.equals("Select Month") && reservationDate.getMonthValue() == getMonthNumber(selectedMonth);

                // Show the reservation if it matches the selected filters (year and/or month)
                if (reservation.getUserId().equals(loggedInUserId)) {
                    if (matchesYear && (selectedMonth.equals("Select Month") || matchesMonth)) {
                        reservationData.add(reservation);  // Add reservation to filtered list
                    }
                }
            }
        }
    }

    // Helper function to convert month name (e.g., "January") to month number (1)
    private int getMonthNumber(String monthName) {
        switch (monthName) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return -1;
        }
    }
}
