package client.login;

import client.signup.SignUpController;
import client.signup.SignUpModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

            //just to make sure all fields are accomplished
            if(userID.isEmpty() || pass.isEmpty() || userType == null) {
                loginView.getPromptLabel().setText("Please accomplish all fields.");
                loginView.getPromptLabel().setVisible(true);
            }else{

                loginView.getPromptLabel().setVisible(false);//hide error prompt if all is good

                //TODO: Include here the authentication logic or any of the likes
            }
        });

        //this button just returns to the sign_up_page
        this.loginView.setActionSignUpButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/sign_up_page.fxml"));
                root = fxmlLoader.load();

                //this is very important because it wouldnt be able to do anything, especially update the model
                //that will communicate with the server base
                new SignUpController(fxmlLoader.getController(), new SignUpModel());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch(IOException e){
                e.printStackTrace();
            }

        });


    }


}
