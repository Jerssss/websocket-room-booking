// File: client/admin/view/AdminMainMenuView.java
package client.admin.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

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
    @FXML
    private ToggleButton serverToggleButton;

    /** Set Toggle Button Action */
    public void setActionToggleButton(EventHandler<ActionEvent> event) {
        serverToggleButton.setOnAction(event);
    }

    /** Check if Toggle is Selected */
    public boolean isServerToggleSelected() {
        return serverToggleButton.isSelected();
    }

    /** Set Text on Toggle Button */
    public void setToggleText(String text) {
        serverToggleButton.setText(text);
    }

    /** Set Logged-in User Name */
    public void setLoggedInUserName(String name) {
        headerNameLabel.setText(name);
    }

    /** Initialize Date and Time */
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

    public void setActionAddNewTerminalButton(EventHandler<ActionEvent> event) {
        addNewTerminalButton.setOnAction(event);
    }

    public void setActionShowStudentReservationButton(EventHandler<ActionEvent> event) {
        showStudentReservationButton.setOnAction(event);
    }

    public void setActionModifyTerminalButton(EventHandler<ActionEvent> event) {
        modifyTerminalButton.setOnAction(event);
    }

    public void setActionResApprovalButton(EventHandler<ActionEvent> event) {
        resApprovalButton.setOnAction(event);
    }

    public void setActionReportsButton(EventHandler<ActionEvent> event) {
        reportsButton.setOnAction(event);
    }

    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logoutButton.setOnAction(event);
    }
}
