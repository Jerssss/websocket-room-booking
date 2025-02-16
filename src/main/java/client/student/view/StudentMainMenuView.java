package client.student.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
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

    private List<Button> menuButtons; // Store all menu buttons

    public void initialize() {
        // Initialize the list of buttons
        menuButtons = List.of(createReservationButton, viewReservationButton, modifyReservationButton);
    }

    // Getter methods for buttons
    public Button getCreateReservationButton() {
        return createReservationButton;
    }

    public Button getViewReservationButton() {
        return viewReservationButton;
    }

    public Button getModifyReservationButton() {
        return modifyReservationButton;
    }

    public void loadView(String fxmlFile) {
        URL fxmlLocation = getClass().getResource(fxmlFile);
        if (fxmlLocation == null) {
            throw new RuntimeException("FXML file not found: " + fxmlFile);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML file: " + fxmlFile, e);
        }
    }

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

        Platform.runLater(() -> {
            headerDateLabel.setText(currentDate.format(dateFormatter));
            headerTimeLabel.setText(currentTime.format(timeFormatter));
        });
    }

    public void setLoggedInUserName(String name) {
        headerNameLabel.setText(name);
    }

    public void setActionCreateReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event);
    }

    public void setActionViewReservationButton(EventHandler<ActionEvent> event) {
        viewReservationButton.setOnAction(event);
    }

    public void setActionModifyReservationButton(EventHandler<ActionEvent> event) {
        modifyReservationButton.setOnAction(event);
    }

    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logOutButton.setOnAction(event);
    }

    public void highlightSelectedButton(Button selectedButton) {
        for (Button button : menuButtons) {
            button.getStyleClass().remove("button-selected");
        }
        selectedButton.getStyleClass().add("button-selected");
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