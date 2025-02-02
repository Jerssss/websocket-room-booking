package client.view.clientview;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;

public class SignInPageView {

    //signin_page.fxml objects for direct interaction/config
    @FXML
    private Button signInPageSignInButton;
    @FXML
    private Button signInPageSignUpButton;
    @FXML
    private TextField idField;
    @FXML
    private PasswordField passField;
    @FXML
    private ComboBox<String> userTypeBox;
    @FXML
    private Label promptLabel;

    private FXMLLoader fxmlLoader;
    private Parent root;

    //action receiver
    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        signInPageSignUpButton.setOnAction(event);
    }

    //action receiver
    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        signInPageSignInButton.setOnAction(event);
    }

    //getters
    public TextField getIDField(){
        return idField;
    }

    public PasswordField getPassField(){
        return passField;
    }

    public Label getPromptLabel() {
        return promptLabel;
    }

    public ComboBox<String> getUserTypeBox() {
        return userTypeBox;
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
