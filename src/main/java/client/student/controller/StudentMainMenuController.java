package client.student.controller;

import client.student.model.CreateReservationModel;
import client.student.model.StudentMainMenuModel;
import client.student.view.CreateReservationView;
import client.student.view.StudentMainMenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentMainMenuController {

    private final StudentMainMenuView studentMainMenuView;
    private final StudentMainMenuModel studentMainMenuModel;


    public StudentMainMenuController(StudentMainMenuView studentMainMenuView, StudentMainMenuModel studentMainMenuModel) {
        this.studentMainMenuView = studentMainMenuView;
        this.studentMainMenuModel = studentMainMenuModel;
        initializeActions();
    }

    private void initializeActions() {
        studentMainMenuView.setActionCreateReservationButton(this::handleCreateReservation);
        studentMainMenuView.setActionViewReservationButton(this::handleViewReservation);
        studentMainMenuView.setActionModifyReservationButton(this::handleModifyReservation);
        studentMainMenuView.setActionLogoutButton(this::handleLogout);
    }

    private void handleCreateReservation(ActionEvent event) {
        System.out.println("Navigating to Create Reservation Page...");
        studentMainMenuView.loadView("/fxml/client/create_reservation_pane.fxml");


        try {
            // Ensure the path to student_main_menu.fxml is correct
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/room_item_card.fxml"));
            Parent root = fxmlLoader.load();

            CreateReservationView CreateReservationView = fxmlLoader.getController();
            new CreateReservationController(CreateReservationView, new CreateReservationModel());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Student Main Menu GUI: " + e.getMessage());
        }
    }

    private void handleViewReservation(ActionEvent event) {
        System.out.println("Navigating to View Reservation Page...");
        studentMainMenuView.loadView("/fxml/client/view_reservation_pane.fxml");
    }

    private void handleModifyReservation(ActionEvent event) {
        System.out.println("Navigating to Modify Reservation Page...");
        studentMainMenuView.loadView("/fxml/client/modify_reservation_pane.fxml");
    }

    private void handleLogout(ActionEvent event) {
        System.out.println("Logging out...");
        // Implement logout functionality
    }
}
