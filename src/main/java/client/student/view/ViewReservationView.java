package client.student.view;

import client.student.controller.ViewReservationController;
import client.utility.SessionManager;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import server.utility.Reservation;
import javafx.fxml.FXML;
import server.student.ViewReservationProcessor;
import client.utility.ServerConnectionManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        System.out.println("ViewReservationView initialized!");
        // Initialize columns and bind properties
        reservationIDColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        terminalIDColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        modResTableView.setItems(reservationData);

        new ViewReservationController(this, sessionToken);

        if (modResTableView == null) {
            System.err.println("TableView is NULL. Check FXML fx:id!");
        } else {
            System.out.println("TableView successfully loaded.");
        }
        reservationData.add(new Reservation("999", "1", "5", "2025-02-20", "10:00", "12:00", "Approved"));

            modResTableView.setItems(reservationData);
            modResTableView.refresh();


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
        System.out.println("🔍 Checking session token before loading data: " + sessionToken);
        if (sessionToken == null) {
            System.err.println("Error: Session token not set!");
            return;
        }
        loadDataForLoggedInUser(sessionToken);
    }

    // Load reservations for the logged-in user only
    private void loadDataForLoggedInUser(String sessionToken) throws IOException {
        String loggedInUserId = SessionManager.getUserId(sessionToken);  // Fetch logged-in user ID
        System.out.println("Logged-in User ID: " + loggedInUserId);  // Debugging logged-in user ID

        if (loggedInUserId == null) {
            System.err.println("Error: Invalid or expired session token!");
            return;
        }

        String xmlPath = "src/main/java/server/util/reservation_approval.xml";
        System.out.println("Debug - Loading XML from: " + new File(xmlPath).getAbsolutePath());

        List<Reservation> reservations = ViewReservationProcessor.parseXML(xmlPath);
        System.out.println("Parsed Reservations: " + reservations);

        if (reservations == null || reservations.isEmpty()) {
            System.err.println("ERROR: No reservations found in XML!");
            return;
        }

        System.out.println("DEBUG: Total Reservations Found: " + reservations.size());

        // Clear existing data before filtering
        reservationData.clear();

        for (Reservation reservation : reservations) {
            String reservationUserId = reservation.getUserId().trim();  // Trim for safety
            String loggedUserIdTrimmed = loggedInUserId.trim();

            System.out.println("Checking reservation - User ID: " + reservationUserId);

            if (reservationUserId.equals(loggedUserIdTrimmed)) {
                System.out.println("✅ Adding Reservation -> ID: " + reservation.getReservationId() +
                        ", User: " + reservation.getUserId() +
                        ", Date: " + reservation.getReservationDate());

                reservationData.add(reservation);
            }
        }

        // Debugging: Print final reservations in the list
        System.out.println("Final Reservations in Table:");
        for (Reservation res : reservationData) {
            System.out.println("🟢 ID: " + res.getReservationId() + " | User ID: " + res.getUserId());
        }

        // Ensure UI updates properly
        Platform.runLater(() -> {
            if (modResTableView == null) {
                System.err.println("ERROR: TableView is NULL. Check FXML fx:id!");
            } else {
                System.out.println("✅ Updating TableView with reservations...");
                modResTableView.setItems(FXCollections.observableArrayList(reservationData));
                modResTableView.refresh();
            }
        });

        System.out.println("DEBUG: Session Token in ViewReservationView: " + sessionToken);
    }
    private void loadReservationsIntoTable() {
        System.out.println("🔄 Loading reservations into TableView...");

        // Debug: Check if reservations are added
        if (reservationData.isEmpty()) {
            System.err.println("⚠️ No reservations available to display!");
        } else {
            System.out.println("✅ Reservations found: " + reservationData.size());
        }

        // Ensure UI updates properly
        Platform.runLater(() -> {
            modResTableView.setItems(null);  // Clear TableView first
            modResTableView.setItems(FXCollections.observableArrayList(reservationData)); // Add updated data
            modResTableView.refresh();  // Force UI update
        });

        System.out.println("✅ TableView updated successfully!");
    }

    // Filter reservations based on selected year and month
    private void filterReservations() {
        String selectedMonth = monthComboBox.getValue();
        String selectedYear = yearComboBox.getValue();

        // Debug: Show selected filters
        System.out.println("Filtering Reservations - Selected Month: " + selectedMonth + ", Selected Year: " + selectedYear);

        reservationData.clear();  // Clear existing data

        String loggedInUserId = SessionManager.getUserId(sessionToken);
        if (loggedInUserId == null) {
            System.err.println("Error: Invalid session token!");
            return;
        }

        List<Reservation> reservations = ViewReservationProcessor.parseXML("src/main/java/server/util/reservation_approval.xml");

        if (reservations != null) {
            for (Reservation reservation : reservations) {
                LocalDate reservationDate = LocalDate.parse(reservation.getReservationDate());

                boolean matchesYear = !selectedYear.equals("Select Year") && String.valueOf(reservationDate.getYear()).equals(selectedYear);
                boolean matchesMonth = !selectedMonth.equals("Select Month") && reservationDate.getMonthValue() == getMonthNumber(selectedMonth);

                // Debug: Print whether a reservation is matching filters
                System.out.println("Checking Reservation - ID: " + reservation.getReservationId() +
                        " | Date: " + reservationDate +
                        " | Matches Year? " + matchesYear +
                        " | Matches Month? " + matchesMonth);

                // Add if it matches the logged-in user and the selected filters
                if (reservation.getUserId().equals(loggedInUserId)) {
                    if (matchesYear && (selectedMonth.equals("Select Month") || matchesMonth)) {
                        System.out.println("Adding Reservation: " + reservation.getReservationId());
                        reservationData.add(reservation);
                    }
                }
            }
        }

        // Refresh table to reflect new data
        modResTableView.setItems(reservationData);
        modResTableView.refresh();
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
