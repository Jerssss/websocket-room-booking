// File: client/admin/view/AdminMainMenuView.java
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
    private Label headerNameLabel;
    @FXML
    private Label headerDateLabel;
    @FXML
    private Label headerTimeLabel;
    @FXML
    private BorderPane rootPane;

    // 🟡 Add this (fixing the error)
    @FXML
    private ToggleButton serverToggleButton;

    /** Load a new view inside the main menu */
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load view: " + fxmlFile);
        }
    }

    /** Set the name of the logged-in user */
    public void setLoggedInUserName(String name) {
        headerNameLabel.setText(name);
    }

    /** Initialize the date and time labels */
    public void initializeDateTime() {
        updateDateTime();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDateTime();
            }
        }, 0, 1000);
    }

    /** Helper method to update the date and time labels */
    private void updateDateTime() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        javafx.application.Platform.runLater(() -> {
            headerDateLabel.setText(currentDate.format(dateFormatter));
            headerTimeLabel.setText(currentTime.format(timeFormatter));
        });
    }

    /** Event handler for Add Terminal Button */
    public void setActionAddNewTerminalButton(EventHandler<ActionEvent> event) {
        addNewTerminalButton.setOnAction(event1 -> loadView("/fxml/admin/add_terminal_pane.fxml"));
    }

    /** Event handler for View Student Reservations Button */
    public void setActionShowStudentReservationButton(EventHandler<ActionEvent> event) {
        showStudentReservationButton.setOnAction(event1 -> loadView("/fxml/admin/view_reservation_pane.fxml"));
    }

    /** Event handler for Modify Terminal Button */
    public void setActionModifyTerminalButton(EventHandler<ActionEvent> event) {
        modifyTerminalButton.setOnAction(event1 -> loadView("/fxml/admin/modify_terminal_pane.fxml"));
    }

    /** Event handler for Reservation Approval Button */
    public void setActionResApprovalButton(EventHandler<ActionEvent> event) {
        resApprovalButton.setOnAction(event1 -> showReservationApprovalView());
    }

    /** Event handler for Reports Button */
    public void setActionReportsButton(EventHandler<ActionEvent> event) {
        reportsButton.setOnAction(event1 -> loadView("/fxml/admin/reports_pane.fxml"));
    }

    /** Event handler for Logout Button */
    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logoutButton.setOnAction(event);
    }

    /** Show Reservation Approval View */
    public void showReservationApprovalView() {
        loadView("/fxml/admin/reservation_approval_pane.fxml");
    }

    /** Event handler for Toggle Button */
    public void setActionToggleButton(EventHandler<ActionEvent> event) {
        serverToggleButton.setOnAction(event);
    }

    /** Check if Toggle Button is Selected */
    public boolean isServerToggleSelected() {
        return serverToggleButton.isSelected();
    }

    /** Set Toggle Button Text */
    public void setToggleText(String text) {
        serverToggleButton.setText(text);
    }

    /** Show Error Dialog */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
