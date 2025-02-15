// File: client/admin/controller/AdminMainMenuController.java
package client.admin.controller;

import client.admin.model.AdminMainMenuModel;
import client.admin.view.AdminMainMenuView;
import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.student.view.StudentMainMenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.utility.LogsXMLHandler;

import java.io.IOException;

public class AdminMainMenuController {

    private final AdminMainMenuView view;
    private final AdminMainMenuModel model;
    private final String loggedInUserName;

    public AdminMainMenuController(AdminMainMenuView view, AdminMainMenuModel model, String loggedInUserName) {
        this.view = view;
        this.model = model;
        this.loggedInUserName = loggedInUserName;

        // Display logged-in user's name
        this.view.setLoggedInUserName(loggedInUserName);
        this.view.initializeDateTime();

        // Handle button actions
        this.view.setActionAddNewTerminalButton(event -> handleAddNewTerminal());
        this.view.setActionModifyTerminalButton(event -> handleModifyTerminal());
        this.view.setActionShowStudentReservationButton(event -> handleViewStudentReservation());
        this.view.setActionResApprovalButton(event -> handleReservationApproval());
        this.view.setActionLogoutButton(this::handleLogout);
    }

    private void handleModifyTerminal() {
        System.out.println("Navigating to Modify Terminal Status...");
    }

    private void handleAddNewTerminal() {
        System.out.println("Navigating to Add New Terminal...");
    }

    private void handleViewStudentReservation() {
        System.out.println("Navigating to View Student Reservations...");
    }

    private void handleReservationApproval() {
        System.out.println("Navigated to Reservation Approval Page.");
    }

    private void handleLogout(ActionEvent event) {
        LogsXMLHandler.logLogout(loggedInUserName, "Admin");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            Parent root = fxmlLoader.load();

            LoginView loginView = fxmlLoader.getController();
            new LoginController(loginView, new LoginModel(), new StudentMainMenuView(), new AdminMainMenuView());

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
