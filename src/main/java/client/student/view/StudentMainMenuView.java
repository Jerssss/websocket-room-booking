package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class StudentMainMenuView {

    @FXML
    private Button createReservationButton;
    @FXML
    private Button viewReservationButton;
    @FXML
    private Button modifyReservationButton;
    @FXML
    private Button logOutButton; // Ensure this matches the fx:id in FXML
    @FXML
    private Label headerNameLabel;
    @FXML
    private Label headerDateLabel;
    @FXML
    private Label headerTimeLabel;
    @FXML
    private BorderPane rootPane;

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

    public void setActionCreateReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event1 -> loadView("/fxml/client/create_reservation_pane.fxml"));
    }

    public void setActionViewReservationButton(EventHandler<ActionEvent> event) {
        viewReservationButton.setOnAction(event1 -> loadView("/fxml/client/view_reservation_pane.fxml"));
    }

    public void setActionModifyReservationButton(EventHandler<ActionEvent> event) {
        modifyReservationButton.setOnAction(event1 -> loadView("/fxml/client/modify_reservation_pane.fxml"));
    }

    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logOutButton.setOnAction(event); // Ensure this is correctly setting the action
    }
}