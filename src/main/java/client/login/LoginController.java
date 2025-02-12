package client.login;

import client.signup.SignUpController;
import client.signup.SignUpModel;
import client.signup.SignUpView;
import client.student.controller.StudentMainMenuController;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import client.admin.controller.AdminMainMenuController;
import client.admin.view.AdminMainMenuView;
import client.admin.model.AdminMainMenuModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private final LoginView loginView;
    private final LoginModel loginModel;
    private final StudentMainMenuView studentMainMenuView;
    private final AdminMainMenuView adminMainMenuView;

    public LoginController(LoginView loginView, LoginModel loginModel, StudentMainMenuView studentMainMenuView, AdminMainMenuView adminMainMenuView) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        this.studentMainMenuView = studentMainMenuView;
        this.adminMainMenuView = adminMainMenuView;

        // Handle Sign In button click
        this.loginView.setActionSignInButton(this::handleSignIn);

        // Handle Sign Up button click
        this.loginView.setActionSignUpButton(event -> redirectToSignUp(event));
    }

    private void handleSignIn(ActionEvent event) {
        // Get user inputs
        String userID = loginView.getIDField().getText();
        String password = loginView.getPassField().getText();
        String userType = loginView.getUserTypeBox().getValue(); // "Student" or "Admin"

        // Validate input fields
        if (userID.isEmpty() || password.isEmpty() || userType == null) {
            loginView.setPromptLabel("Please complete all fields.");
            loginView.setPromptLabelVisible(true);
            return;
        }

        // Send login data to the server via LoginModel
        boolean isAuthenticated = loginModel.authenticate(userID, password, userType);

        // Show result in the UI
        if (isAuthenticated) {
            loginView.setPromptLabel("Login successful!");
            loginView.setPromptLabelVisible(true);

            // Navigate to Student Main Menu if user type is "Student"
            if ("Student".equalsIgnoreCase(userType)) {
                redirectToStudentMainMenu(event);
            } else if ("Admin".equalsIgnoreCase(userType)) {
                redirectToAdminMainMenu(event);
            }
            // Optionally, add logic to navigate to the Admin dashboard for admin users.
        } else {
            loginView.setPromptLabel("Invalid credentials. Please try again.");
            loginView.setPromptLabelVisible(true);
        }
    }
    private void redirectToStudentMainMenu(ActionEvent event) {
        try {
            // Ensure the path to student_main_menu.fxml is correct
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/student_main_menu.fxml"));
            Parent root = fxmlLoader.load();

            // Initialize StudentMainMenuController (if required)
            StudentMainMenuView studentMainMenuView = fxmlLoader.getController();
            new StudentMainMenuController(studentMainMenuView, new StudentMainMenuModel());
            // studentMainMenuView.setUser(userID);

            // Navigate to the Student Main Menu GUI
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Student Main Menu GUI: " + e.getMessage());
        }
    }

    private void redirectToAdminMainMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_menu_page.fxml"));
            Parent root = fxmlLoader.load();

            // Ensure that the correct controller is linked
            AdminMainMenuView adminMainMenuView = fxmlLoader.getController();
            new AdminMainMenuController(adminMainMenuView, new AdminMainMenuModel());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Admin Main Menu GUI: " + e.getMessage());
        }
    }


    private void redirectToSignUp(ActionEvent event) {
        try {
            // Ensure the path to signup_page.fxml is correct
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/sign_up_page.fxml"));
            Parent root = fxmlLoader.load();

            // Initialize the SignUpController with the loaded view and model
            SignUpView signUpView = fxmlLoader.getController();
            new SignUpController(signUpView, new SignUpModel());

            // Navigate to the Sign-Up GUI
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Sign-Up GUI: " + e.getMessage());
        }
    }

}
