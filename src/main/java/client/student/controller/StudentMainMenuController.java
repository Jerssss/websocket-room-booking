package client.student.controller;

import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import javafx.event.ActionEvent;

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
