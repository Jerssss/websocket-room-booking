package client.view.clientview;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class SignInPageView {

    //signin_page.fxml objects for direct interaction/config
    @FXML
    private Button signInPageSignInButton;
    @FXML
    private Button signInPageSignUpButton;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passField;

    private FXMLLoader fxmlLoader;
    private Parent root;

    //action receiver from the SignInPageController
    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        signInPageSignUpButton.setOnAction(event);
    }

    //getters
    public TextField getNameField(){
        return nameField;
    }

    public PasswordField getPassField(){
        return passField;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public Parent getRoot() {
        return root;
    }

    //setters
    public void setFxmlLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    //effects for sign in button
    public void signInPageSignInButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), signInPageSignInButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void signInPageSignInButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), signInPageSignInButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    //for sign up button
    public void signInPageSignUpButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), signInPageSignUpButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void signInPageSignUpButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), signInPageSignUpButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}
