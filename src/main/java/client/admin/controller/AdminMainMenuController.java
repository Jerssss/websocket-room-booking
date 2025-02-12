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

import java.io.IOException;

public class AdminMainMenuController {

    private final AdminMainMenuView view;
    private final AdminMainMenuModel model;

    public AdminMainMenuController(AdminMainMenuView view, AdminMainMenuModel model, String loggedInUserName) {
        this.view = view;
        this.model = model;

        // Set the logged-in user's name
        this.view.setLoggedInUserName(loggedInUserName);

        // Initialize date and time labels
        this.view.initializeDateTime();

        // Set up button actions
        this.view.setActionAddNewTerminalButton(event -> handleAddNewTerminal());
        this.view.setActionLogoutButton(event -> handleLogout(event));
    }

    private void handleAddNewTerminal() {
        System.out.println("Navigating to Add New Terminal...");
    }

    private void handleLogout(ActionEvent event) {
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