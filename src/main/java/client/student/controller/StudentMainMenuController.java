// File: client/student/controller/StudentMainMenuController.java
package client.student.controller;

import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import client.admin.view.AdminMainMenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.utility.LogsXMLHandler;

import java.io.IOException;

public class StudentMainMenuController {
    private final StudentMainMenuView view;
    private final StudentMainMenuModel model;
    private final String loggedInUserName;

    private Thread serverThread;  // Server Thread

    public StudentMainMenuController(StudentMainMenuView view, StudentMainMenuModel model, String loggedInUserName) {
        this.view = view;
        this.model = model;
        this.loggedInUserName = loggedInUserName;

        // Display logged-in user's name
        this.view.setLoggedInUserName(loggedInUserName);
        this.view.initializeDateTime();

        // Handle button actions
        this.view.setActionCreateReservationButton(event -> handleCreateReservation());
        this.view.setActionViewReservationButton(event -> handleViewReservation());
        this.view.setActionModifyReservationButton(event -> handleModifyReservation());
        this.view.setActionLogoutButton(this::handleLogout);
    }

    private void handleCreateReservation() {
        System.out.println("Navigating to Add New Terminal...");
    }

    private void handleViewReservation() {
        System.out.println("Navigating to View Student Reservations...");
    }

    private void handleModifyReservation() {
        System.out.println("Navigating to Modify Terminal Status...");
    }

    /** Handle Logout Action and Log to logs.xml */
    private void handleLogout(ActionEvent event) {
        // Log the logout action
        LogsXMLHandler.logLogout(loggedInUserName, "Student");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            Parent root = loader.load();

            // Reinitialize LoginController with proper references
            LoginView loginView = loader.getController();
            new LoginController(
                    loginView,
                    new LoginModel(),
                    new StudentMainMenuView(),
                    new AdminMainMenuView()
            );

            // Switch back to login screen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading login page: " + e.getMessage());
        }
    }
}
