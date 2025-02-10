package client.login;

import client.signup.SignUpController;
import client.signup.SignUpModel;
import client.signup.SignUpView;
import client.student.view.StudentMainMenuView;
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

    public LoginController(LoginView loginView, LoginModel loginModel, StudentMainMenuView studentMainMenuView) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        this.studentMainMenuView = studentMainMenuView;

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
            // Navigate to the appropriate dashboard (Student/Admin)
        } else {
            loginView.setPromptLabel("Invalid credentials. Please try again.");
            loginView.setPromptLabelVisible(true);
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
