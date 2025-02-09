package client.login;

import javafx.event.ActionEvent;

public class LoginController {

    private final LoginView loginView;
    private final LoginModel loginModel;

    public LoginController(LoginView loginView, LoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;

        // Handle Sign In button click
        this.loginView.setActionSignInButton(this::handleSignIn);

        // Handle Sign Up button click
        this.loginView.setActionSignUpButton(event -> {
            loginView.setPromptLabel("Redirecting to sign-up...");
            loginView.setPromptLabelVisible(true);
            // Add navigation logic to the sign-up view here
        });
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
}
