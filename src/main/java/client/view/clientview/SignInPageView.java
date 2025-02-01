package client.view.clientview;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInPageView {
    @FXML
    private Button signUpButton;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passField;

    private FXMLLoader fxmlLoader;
    private Parent root;

    //action receiver from the SignInPageController
    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        signUpButton.setOnAction(event);
    }





}
