package client.admin.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class AdminMainMenuView {

    @FXML
    private Button addNewTerminalButton;
    @FXML
    private Button showStudentReservationButton;
    @FXML
    private Button modifyTerminalButton;
    @FXML
    private Button resApprovalButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label headerNameLabel; // Ensure this matches the fx:id in FXML
    @FXML
    private Label headerDateLabel; // Ensure this matches the fx:id in FXML
    @FXML
    private Label headerTimeLabel; // Ensure this matches the fx:id in FXML
    @FXML
    private BorderPane rootPane;

    // Load a new view inside the main menu
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to set the name of the logged-in user
    public void setLoggedInUserName(String Name) {
        headerNameLabel.setText(Name);
    }

    // Method to initialize the date and time labels
    public void initializeDateTime() {
        // Set initial date and time
        updateDateTime();

        // Schedule a timer to update the time every second
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDateTime();
            }
        }, 0, 1000); // Update every second
    }

    // Helper method to update the date and time labels
    private void updateDateTime() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Format the date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Update the labels on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            headerDateLabel.setText(currentDate.format(dateFormatter));
            headerTimeLabel.setText(currentTime.format(timeFormatter));
        });
    }

    public void setActionAddNewTerminalButton(EventHandler<ActionEvent> event) {
        addNewTerminalButton.setOnAction(event1 -> loadView("/fxml/admin/add_terminal_pane.fxml"));
    }

    public void setActionShowStudentReservationButton(EventHandler<ActionEvent> event) {
        showStudentReservationButton.setOnAction(event1 -> loadView("/fxml/admin/student_reservations_pane.fxml"));
    }

    public void setActionModifyTerminalButton(EventHandler<ActionEvent> event) {
        modifyTerminalButton.setOnAction(event1 -> loadView("/fxml/admin/modify_terminal_pane.fxml"));
    }

    public void setActionResApprovalButton(EventHandler<ActionEvent> event) {
        resApprovalButton.setOnAction(event1 -> {
            System.out.println("Loading Reservation Approval View...");
            loadView("/fxml/admin/reservation_approval_pane.fxml");
        });
    }


    public void setActionReportsButton(EventHandler<ActionEvent> event) {
        reportsButton.setOnAction(event1 -> loadView("/fxml/admin/modify_reservation_pane.fxml"));
    }

    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logoutButton.setOnAction(event);
    }
}