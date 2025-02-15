package client.landingpage;

import client.login.LoginView;
import client.signup.SignUpView;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class LandingPageView {
    @FXML
    private Button landingPageLogInButton;
    @FXML
    private Button landingPageSignUpButton;
    @FXML
    private LoginView loginView;
    @FXML
    private SignUpView signUpView;

    private FXMLLoader fxmlLoader;
    private Parent root;


    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        landingPageLogInButton.setOnAction(event);
    }

    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        landingPageSignUpButton.setOnAction(event);
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

    public void signUpButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageSignUpButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void signUpButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageSignUpButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void logInButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageLogInButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void logInButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageLogInButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

}
