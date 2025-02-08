package client.login;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;

public class LoginView {

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

}

