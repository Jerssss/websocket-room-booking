package client.landingpage;

import client.login.LoginController;
import client.login.LoginModel;
import client.signup.SignUpController;
import client.signup.SignUpModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {

    private FXMLLoader fxmlLoader;
    private Parent root;
    private LoginController loginController;
    private SignUpController signUpController;

    public LandingPageController (LandingPageView view) {

        view.setActionSignInButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = fxmlLoader.load();

                loginController = new LoginController(fxmlLoader.getController(), new LoginModel());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });

        view.setActionSignUpButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/sign_up_page.fxml"));
                root = fxmlLoader.load();

                signUpController = new SignUpController(fxmlLoader.getController(), new SignUpModel());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
    }
    public FXMLLoader getfxmlLoader() {
        return fxmlLoader;
    }

    public Parent getRoot() {
        return root;
    }
}
