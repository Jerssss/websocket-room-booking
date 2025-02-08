package client.signup;

import client.landingpage.LandingPageController;
import client.login.LoginController;
import client.login.LoginModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class SignUpController {

    private FXMLLoader fxmlLoader;
    private Parent root; //takes in the root node of the fxml file (like in xml files)
    private final SignUpView signUpView;



    public SignUpController(SignUpView signUpView, SignUpModel signUpModel) {
        this.signUpView = signUpView;


        this.signUpView.setActionSignInButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/signin_page.fxml"));
                root = fxmlLoader.load(); //saves loaded fxml file's root node into the object root

                new LoginController(fxmlLoader.getController(), new LoginModel());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //sets the stage or window to display the scene
                Scene scene = new Scene(root); //a new scene or window is created with the root node (which contains all the elements in the fxml file)
                stage.setScene(scene);
                stage.show();//display
            }catch(IOException e){
                e.printStackTrace();
            }
        });

        this.signUpView.setActionSignUpButton((ActionEvent event) -> {
            //store field and dropdown contents
            String userID = signUpView.getIDField().getText();
            String pass = signUpView.getPassField().getText();
            String userType = signUpView.getUserTypeBox().getValue();

            //prompter when fields are unaccomplished
            if(userID.isEmpty() || pass.isEmpty() || userType == null) {
                signUpView.getPromptLabel().setText("Please accomplish all fields.");
                signUpView.getPromptLabel().setVisible(true);
            }else{
                signUpView.getPromptLabel().setVisible(false);//hide error prompt if all is good

//                try{
//                    TODO: include authentication logic or method call for authentication and server response
//                }
            }
        });



    }
}
