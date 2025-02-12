package client.student.controller;

import client.admin.view.AdminMainMenuView;
import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.student.model.StudentMainMenuModel;
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
    private final String loggedInUserName;

    public StudentMainMenuController(StudentMainMenuView studentMainMenuView, StudentMainMenuModel studentMainMenuModel, String loggedInUserName) {
        this.studentMainMenuView = studentMainMenuView;
        this.studentMainMenuModel = studentMainMenuModel;
        this.loggedInUserName = loggedInUserName;

        //  Set user name in the UI
        this.studentMainMenuView.setLoggedInUserName(loggedInUserName);
        this.studentMainMenuView.initializeDateTime();

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            Parent root = fxmlLoader.load();

            // Ensure that the LoginController is properly initialized
            LoginView loginView = fxmlLoader.getController();
            new LoginController(loginView, new LoginModel(), new StudentMainMenuView(), new AdminMainMenuView());

            // Get the current stage and switch to the login scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully logged out and redirected to the login page.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Login GUI: " + e.getMessage());
        }
    }
}
