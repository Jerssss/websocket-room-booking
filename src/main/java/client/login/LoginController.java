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
import client.utility.SessionManager;
import client.utility.SessionTokenGenerator;
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
    private String sessionToken;

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

        // Authenticate and retrieve user name
        String userName = loginModel.authenticateAndGetName(userID, password, userType);

        if (userName != null) {
            loginView.setPromptLabel("Login successful!");
            loginView.setPromptLabelVisible(true);

            // Generate a session token (replace with your actual token generator)
            String sessionToken = SessionTokenGenerator.generateUniqueToken();

            // Store the session in SessionManager
            SessionManager.createSession(sessionToken, userID);
            // Pass the user's NAME (not ID) to the main menu
            if ("Student".equalsIgnoreCase(userType)) {
                redirectToStudentMainMenu(event, userName, sessionToken);
            } else if ("Admin".equalsIgnoreCase(userType)) {
                redirectToAdminMainMenu(event, userName);
            }
        } else {
            loginView.setPromptLabel("Invalid credentials. Please try again.");
            loginView.setPromptLabelVisible(true);
        }
    }

    private void redirectToStudentMainMenu(ActionEvent event, String loggedInUserName, String sessionToken) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/student_main_menu.fxml"));
            Parent root = fxmlLoader.load();

            //  Ensure the controller receives the logged-in user's name
            StudentMainMenuView studentMainMenuView = fxmlLoader.getController();
            new StudentMainMenuController(
                    studentMainMenuView,
                    new StudentMainMenuModel(),
                    loggedInUserName,
                    sessionToken
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Student Main Menu GUI: " + e.getMessage());
        }
    }


    private void redirectToAdminMainMenu(ActionEvent event, String loggedInUserName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_menu_page.fxml"));
            Parent root = fxmlLoader.load();

            // Ensure that the correct controller is linked
            AdminMainMenuView adminMainMenuView = fxmlLoader.getController();
            new AdminMainMenuController(adminMainMenuView, new AdminMainMenuModel(), loggedInUserName);

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
