package client.student.view;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StudentMainMenuView {

    @FXML
    private Button createReservationButton;
    @FXML
    private Button viewReservationButton;
    @FXML
    private Button modifyReservationButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label Name;
    @FXML
    private Label Date;
    @FXML
    private Label Time;
    @FXML
    private BorderPane rootPane;


    private void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setActionCreateReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event1 -> loadView("create_reservation_pane.fxml"));
    }


    public void setActionViewReservationButton(EventHandler<ActionEvent> event) {
        viewReservationButton.setOnAction(event1 -> loadView("view_reservation_pane.fxml"));
    }
    public void setActionModifyReservationButton(EventHandler<ActionEvent> event) {
        modifyReservationButton.setOnAction(event1 -> loadView("modify_reservation_pane.fxml"));
    }
    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logoutButton.setOnAction(event);
    }

}




