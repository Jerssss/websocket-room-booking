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
    private Button logoutButton; // Ensure this matches the fx:id in FXML
    @FXML
    private Label Name;
    @FXML
    private Label Date;
    @FXML
    private Label Time;
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

    public void setActionAddNewTerminalButton(EventHandler<ActionEvent> event) {
        addNewTerminalButton.setOnAction(event1 -> loadView("/fxml/admin/add_terminal_pane.fxml")); // Correct path
    }

    public void setActionShowStudentReservationButton(EventHandler<ActionEvent> event) {
        showStudentReservationButton.setOnAction(event1 -> loadView("/fxml/admin/view_reservation_pane.fxml"));
    }

    public void setActionModifyTerminalButton(EventHandler<ActionEvent> event) {
        modifyTerminalButton.setOnAction(event1 -> loadView("/fxml/admin/modify_reservation_pane.fxml"));
    }

    public void setActionResApprovalButton(EventHandler<ActionEvent> event) {
        resApprovalButton.setOnAction(event1 -> loadView("/fxml/admin/modify_reservation_pane.fxml"));
    }

    public void setActionReportsButton(EventHandler<ActionEvent> event) {
        reportsButton.setOnAction(event1 -> loadView("/fxml/admin/modify_reservation_pane.fxml"));
    }

    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logoutButton.setOnAction(event); // Ensure this is correctly setting the action
    }
}