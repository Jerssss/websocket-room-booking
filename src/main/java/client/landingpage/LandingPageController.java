package client.landingpage;

import client.login.LoginController;
import client.login.LoginModel;
import client.signup.SignUpController;
import client.signup.SignUpModel;
import client.student.view.StudentMainMenuView;
import client.admin.view.AdminMainMenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import javafx.application.Platform;
import java.io.IOException;
import java.net.Socket;

public class LandingPageController {

    private FXMLLoader fxmlLoader;
    private Parent root;
    private LoginController loginController;
    private SignUpController signUpController;

    StudentMainMenuView studentMainMenuView;
    AdminMainMenuView adminMainMenuView;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public LandingPageController(LandingPageView view) {

        view.setActionSignInButton((ActionEvent event) -> {
            if (!isServerAvailable()) {
                showErrorDialog("Server is down or unreachable. Please try again later.");
                return;
            }

            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = fxmlLoader.load();

                loginController = new LoginController(fxmlLoader.getController(), new LoginModel(), studentMainMenuView, adminMainMenuView);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                showErrorDialog("Error loading the login page. Please try again.");
            }
        });

        view.setActionSignUpButton((ActionEvent event) -> {
            if (!isServerAvailable()) {
                showErrorDialog("Server is down or unreachable. Please try again later.");
                return;
            }

            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/sign_up_page.fxml"));
                root = fxmlLoader.load();

                signUpController = new SignUpController(fxmlLoader.getController(), new SignUpModel());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                showErrorDialog("Error loading the sign-up page. Please try again.");
            }
        });
    }

    private boolean isServerAvailable() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            return true; // Connection successful
        } catch (IOException e) {
            return false; // Server is down
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }

    public FXMLLoader getfxmlLoader() {
        return fxmlLoader;
    }

    public Parent getRoot() {
        return root;
    }
}
