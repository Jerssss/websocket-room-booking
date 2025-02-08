package client.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    private FXMLLoader fxmlLoader;
    private Parent root;
    private final LoginView loginView;
    private final LoginModel loginModel;


    public LoginController(LoginView loginView, LoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;

        this.loginView.setActionSignInButton((ActionEvent event) -> {
            //store field and dropdown contents
            String userID = loginView.getIDField().getText();
            String pass = loginView.getPassField().getText();
            String userType = loginView.getUserTypeBox().getValue();

            if(userID.isEmpty() || pass.isEmpty() || userType == null) {
                loginView.getPromptLabel().setText("Please accomplish all fields.");
                loginView.getPromptLabel().setVisible(true);
            }else{

                loginView.getPromptLabel().setVisible(false);//hide error prompt if all is good

            }
        });


    }


}
