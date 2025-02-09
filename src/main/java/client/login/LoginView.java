package client.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginView {

    //login_page.fxml objects for direct interaction/config
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

    // Getters for UI components
    public TextField getIDField() {
        return idField;
    }

    public PasswordField getPassField() {
        return passField;
    }

    public ComboBox<String> getUserTypeBox() {
        return userTypeBox;
    }

    public Label getPromptLabel() {
        return promptLabel;
    }

    // New methods for setting promptLabel text and visibility
    public void setPromptLabel(String text) {
        promptLabel.setText(text);
    }

    public void setPromptLabelVisible(boolean visible) {
        promptLabel.setVisible(visible);
    }

    // Event handlers for button actions
    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        signInPageSignInButton.setOnAction(event);
    }

    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        signInPageSignUpButton.setOnAction(event);
    }
}
