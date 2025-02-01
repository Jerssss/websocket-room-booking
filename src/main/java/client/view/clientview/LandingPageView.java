package client.view.clientview;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class LandingPageView {

    @FXML
    private SignInPageView signInPageView;
    @FXML
    private Button signinButtonLandingPage;
    @FXML
    private Button landingPageSignUpButton;
    @FXML
    private SignInPageView loginPageView;

    private Parent root;
    private FXMLLoader fxmlLoader;

    //adds action receiver to button landingPageSignInButton
    public void setActionSignInButton(EventHandler<ActionEvent> event){
        signinButtonLandingPage.setOnAction(event);
    }
    //adds action receiver to button landingPageSignUpButton
    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        landingPageSignUpButton.setOnAction(event);
    }
    public void lpSignInButtonEntered () {
        signinButtonLandingPage.setStyle("-fx-background-color: #c7a97f;");
    }
    public void lpSignInButtonExit(){
        signinButtonLandingPage.setStyle("-fx-background-color:  #A38157;");
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
}
