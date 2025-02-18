package client.student.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import javafx.util.Duration;

public class StudentMainMenuView {

    @FXML
    private Button createReservationButton;
    @FXML
    private Button viewReservationButton;
    @FXML
    private Button modifyReservationButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Label headerNameLabel;
    @FXML
    private Label headerDateLabel;
    @FXML
    private Label headerTimeLabel;
    @FXML
    private BorderPane rootPane;

    private String sessionToken;

    private void loadView(String fxmlFile) {
        try {
            System.out.println("Loading FXML: " + fxmlFile);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            if (fxmlLoader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found: " + fxmlFile);
            }
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            showError("Failed to load view: " + fxmlFile);
        }
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        System.out.println("DEBUG: Session token set in StudentMainMenuView: " + sessionToken);
    }

    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logOutButton.setOnAction(event);
    }

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

    public void setActionCreateReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event1 -> loadView("/fxml/client/create_reservation_pane.fxml"));
    }

    /** Event handler for View  Reservations Button */
    public void setActionViewReservationButton(EventHandler<ActionEvent> event) {
        viewReservationButton.setOnAction(event1 -> {
            try {
                // Load the ViewReservationView FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/view_reservation_pane.fxml"));
                VBox view = loader.load();

                // Get the controller and set the sessionToken
                ViewReservationView viewController = loader.getController();
                viewController.setSessionToken(sessionToken); // Pass the token here

                // Add the view to the UI
                rootPane.setCenter(view);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to load view: /fxml/client/view_reservation_pane.fxml");
            }
        });
    }

    /** Event handler for Modify Reservation Button */
    public void setActionModifyReservationButton(EventHandler<ActionEvent> event) {
        modifyReservationButton.setOnAction(event1 -> loadView("/fxml/client/modify_reservation_pane.fxml"));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void logOutButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), logOutButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void logOutButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), logOutButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public BorderPane getBorderPane() {
        return this.rootPane;
    }

}