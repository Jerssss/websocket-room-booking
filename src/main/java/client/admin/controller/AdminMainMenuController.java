// File: client/admin/controller/AdminMainMenuController.java
package client.admin.controller;

import client.admin.model.AdminMainMenuModel;
import client.admin.view.AdminMainMenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.ServerMain;
import client.login.LoginView;
import client.login.LoginController;
import client.login.LoginModel;
import client.student.view.StudentMainMenuView;



import java.io.IOException;

public class AdminMainMenuController {

    private final AdminMainMenuView view;
    private final AdminMainMenuModel model;
    private final String loggedInUserName;

    private Thread serverThread;  // Server Thread

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
        this.view.setActionToggleButton(event -> handleServerToggleButton());
        this.view.setActionLogoutButton(this::handleLogout);
    }

    /** Handle Toggle Button to Start/Stop Server */
    public void handleServerToggleButton() {
        if (view.isServerToggleSelected()) {
            // Start Server
            view.setToggleText("STOP");
            serverThread = new Thread(ServerMain::startServer);
            serverThread.start();
            System.out.println("Server Started");
        } else {
            // Stop Server
            view.setToggleText("START");
            ServerMain.stopServer();
            System.out.println("Server Stopped");
        }
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
        System.out.println("");
    }




    // File: AdminMainMenuController.java (same for StudentMainMenuController)
    private void handleLogout(ActionEvent event) {
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
