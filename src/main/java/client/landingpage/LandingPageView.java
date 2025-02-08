package client.landingpage;

import client.login.LoginView;
import client.signup.SignUpView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class LandingPageView {
    @FXML
    private Button signInButtonLandingPage;
    @FXML
    private Button signUpButtonLandingPage;
    @FXML
    private LoginView loginView;
    @FXML
    private SignUpView signUpView;

    private FXMLLoader fxmlLoader;
    private Parent root;


    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        signInButtonLandingPage.setOnAction(event);

    }

    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        signUpButtonLandingPage.setOnAction(event);
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }
}
