package client.signup;

import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.student.controller.StudentMainMenuController;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SignUpController {

    private FXMLLoader fxmlLoader;
    private Parent root; // takes in the root node of the fxml file
    private final SignUpView signUpView;
    private final SignUpModel signUpModel;

    private final StudentMainMenuView studentMainMenuView = new StudentMainMenuView();

    public SignUpController(SignUpView signUpView, SignUpModel signUpModel) {
        this.signUpView = signUpView;
        this.signUpModel = signUpModel;

        // Handle Sign In button click
        this.signUpView.setActionSignInButton(this::redirectToLogin);

        // Handle Sign Up button click
        this.signUpView.setActionSignUpButton(this::handleSignUp);
    }

    private void redirectToLogin(ActionEvent event) {
        try {
            // Ensure the path to login_page.fxml is correct
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            Parent root = fxmlLoader.load();

            // Initialize the LoginController with the loaded view and model
            LoginView loginView = fxmlLoader.getController();
            new LoginController(loginView, new LoginModel(), new StudentMainMenuView());

            // Navigate to the Login GUI
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Login GUI: " + e.getMessage());
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


    private void handleSignUp(ActionEvent event) {
        // Store field and dropdown contents
        String userID = signUpView.getIDField().getText();
        String name = signUpView.getNameField().getText();
        String pass = signUpView.getPassField().getText();
        String userType = signUpView.getUserTypeBox().getValue();
        String courseYear = signUpView.getCourseYearField().getText();
        String facultyType = signUpView.getFacultyTypeField().getText();

        // Prompt when fields are incomplete
        if (userID.isEmpty() || name.isEmpty() || pass.isEmpty() || userType == null) {
            signUpView.getPromptLabel().setText("Please accomplish all fields.");
            signUpView.getPromptLabel().setVisible(true);
        } else {
            signUpView.getPromptLabel().setVisible(false); // Hide error prompt if all is good

            // Call the register method in SignUpModel
            boolean isRegistered = signUpModel.register(userID, name, pass, userType, courseYear, facultyType);

            if (isRegistered) {
                signUpView.getPromptLabel().setText("Registration successful!");
                signUpView.getPromptLabel().setVisible(true);
                redirectToLogin(event);
                // Navigate to the login page after successful sign-up
            } else {
                signUpView.getPromptLabel().setText("Registration failed. Please try again.");
                signUpView.getPromptLabel().setVisible(true);
            }
        }
    }
}
