package client.admin.controller;

import client.admin.model.AdminMainMenuModel;
import client.admin.view.AdminMainMenuView;
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
            // Load the login page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            Parent root = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to the login page
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Login GUI: " + e.getMessage());
        }
    }
}