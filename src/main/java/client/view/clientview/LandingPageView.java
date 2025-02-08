package client.view.clientview;

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
    private Button landingPageSignInButton; //matching the fx:id in the .fxml file for direct interaction
    @FXML
    private Button landingPageSignUpButton;

    private Parent root;
    private FXMLLoader fxmlLoader;

    //adds action receiver to button landingPageSignInButton
    public void setActionSignInButton(EventHandler<ActionEvent> event){
        landingPageSignInButton.setOnAction(event);
    }

    //adds action receiver to button landingPageSignUpButton
    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        landingPageSignUpButton.setOnAction(event);
    }

    //getters
    public FXMLLoader getFXMLLoader(){
        return fxmlLoader;
    }
    public Parent getRoot() {
        return root;
    }

    //setters
    public void setFXMLLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }
    public void setRoot(Parent root) {
        this.root = root;
    }


    //effects for sign in button
    public void landingPageSignInButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageSignInButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void landingPageSignInButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageSignInButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    //for sign up button
    public void landingPageSignUpButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageSignUpButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void landingPageSignUpButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageSignUpButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}
