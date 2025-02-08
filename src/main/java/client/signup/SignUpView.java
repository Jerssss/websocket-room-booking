package client.signup;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;


public class SignUpView {

    @FXML
    private Button landingPageSignInButton; //matching the fx:id in the .fxml file for direct interaction
    @FXML
    private Button landingPageSignUpButton;
    @FXML
    private ComboBox<String> signUpUserTypePicker;
    @FXML
    private TextField signUpUserID;
    @FXML
    private PasswordField signUpUserPass;
    @FXML
    private Label signUpPromptLabel;

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
    public TextField getIDField() {
        return signUpUserID;
    }
    public PasswordField getPassField() {
        return signUpUserPass;
    }
    public ComboBox<String> getUserTypeBox() {
        return signUpUserTypePicker;
    }

    public Label getPromptLabel() {
        return signUpPromptLabel;
    }

    //setters
    public void setFXMLLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }
    public void setRoot(Parent root) {
        this.root = root;
    }

    public void setPromptLabel(Label signUpPromptLabel) {
        this.signUpPromptLabel = signUpPromptLabel;
    }
}
