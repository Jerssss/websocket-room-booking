package client.student.controller;

import client.admin.view.AdminMainMenuView;
import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import client.student.view.ViewReservationView;
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
    private String sessionToken;

    public StudentMainMenuController(StudentMainMenuView studentMainMenuView,
                                     StudentMainMenuModel studentMainMenuModel,
                                     String loggedInUserName,
                                     String sessionToken) {
        this.studentMainMenuView = studentMainMenuView;
        this.studentMainMenuModel = studentMainMenuModel;
        this.loggedInUserName = loggedInUserName;
        this.sessionToken = sessionToken;

        // Set user name in the UI
        this.studentMainMenuView.setLoggedInUserName(loggedInUserName);
        this.studentMainMenuView.initializeDateTime();

        // Initialize button actions
        initializeActions();

        System.out.println("DEBUG: Session token in controller: " + sessionToken);
    }

    private void initializeActions() {
        // Set action handlers for buttons
        studentMainMenuView.setActionCreateReservationButton(this::handleCreateReservation);
        studentMainMenuView.setActionViewReservationButton(this::handleViewReservation);
        studentMainMenuView.setActionModifyReservationButton(this::handleModifyReservation);
        studentMainMenuView.setActionLogoutButton(this::handleLogout);
    }

    private void handleCreateReservation(ActionEvent event) {
        System.out.println("Navigating to Create Reservation Page...");
        studentMainMenuView.loadView("/fxml/client/create_reservation_pane.fxml");
        studentMainMenuView.highlightSelectedButton(studentMainMenuView.getCreateReservationButton());
    }

    private void handleViewReservation(ActionEvent event) {
        System.out.println("Navigating to View Reservation Page...");
        try {
            // Load the reservation pane into the center of the root BorderPane
            studentMainMenuView.loadView("/fxml/client/view_reservation_pane.fxml");

            // Pass session token to the reservation controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/view_reservation_pane.fxml"));
            loader.load();
            ViewReservationView controller = loader.getController();
            controller.setSessionToken(sessionToken);
            controller.loadReservationData();

            // Highlight the selected button
            studentMainMenuView.highlightSelectedButton(studentMainMenuView.getViewReservationButton());
        } catch (IOException e) {
            System.err.println("Failed to load reservation pane: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleModifyReservation(ActionEvent event) {
        System.out.println("Navigating to Modify Reservation Page...");
        studentMainMenuView.loadView("/fxml/client/modify_reservation_pane.fxml");
        studentMainMenuView.highlightSelectedButton(studentMainMenuView.getModifyReservationButton());
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