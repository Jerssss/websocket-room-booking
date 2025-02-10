package client.signup;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SignUpView {

    @FXML
    private Button landingPageSignInButton;
    @FXML
    private Button landingPageSignUpButton;
    @FXML
    private ComboBox<String> signUpUserTypePicker;
    @FXML
    private TextField signUpUserID;
    @FXML
    private TextField facultyTypeField;
    @FXML
    private TextField courseYearField;
    @FXML
    private TextField nameField; // Added Name Field
    @FXML
    private PasswordField signUpUserPass;
    @FXML
    private Label signUpPromptLabel;
    @FXML
    private StackPane facultyTypeStackPane;
    @FXML
    private StackPane courseYearStackPane;

    private Parent root;
    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {
        signUpUserTypePicker.getItems().addAll("Student", "Admin");
        signUpUserTypePicker.setValue("Student");

        updateFormFields();

        signUpUserTypePicker.setOnAction((event -> updateFormFields()));
    }

    private void updateFormFields() {
        String selectedType = signUpUserTypePicker.getValue();
        boolean isStudent = "Student".equals(selectedType);

        courseYearStackPane.setVisible(isStudent);
        courseYearStackPane.setManaged(isStudent);

        courseYearField.setVisible(isStudent);
        courseYearField.setManaged(isStudent);

        facultyTypeField.setVisible(!isStudent);
        facultyTypeField.setManaged(!isStudent);

        facultyTypeStackPane.setVisible(!isStudent);
        facultyTypeStackPane.setManaged(!isStudent);
    }

    // Adds action receiver to button landingPageSignInButton
    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        landingPageSignInButton.setOnAction(event);
    }

    // Adds action receiver to button landingPageSignUpButton
    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        landingPageSignUpButton.setOnAction(event);
    }

    // Getters
    public TextField getIDField() {
        return signUpUserID;
    }

    public TextField getNameField() { // Getter for the Name field
        return nameField;
    }

    public TextField getCourseYearField() {
        return courseYearField;
    }

    public TextField getFacultyTypeField() {
        return facultyTypeField;
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
}
