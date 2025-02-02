package client.controller.signin;

import client.controller.landingpage.LandingPageController;
import client.model.scenemodels.SignInPageModel;
import client.view.clientview.SignInPageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class SignInPageController {

    private final SignInPageView signInPageView;
    private final SignInPageModel signInPageModel;
    private FXMLLoader fxmlLoader;
    private Parent root;



    public SignInPageController(SignInPageView signInView, SignInPageModel signInModel){
        this.signInPageView = signInView;
        this.signInPageModel = signInModel;

//        sign up button action listener
        this.signInPageView.setActionSignUpButton((ActionEvent event) -> {

            //store field and dropdown contents
            String userID = signInView.getIDField().getText();
            String pass = signInView.getPassField().getText();
            String userType = signInView.getUserTypeBox().getValue();

            if(userID.isEmpty() || pass.isEmpty() || userType == null) {
                signInView.getPromptLabel().setText("Please accomplish all fields.");
                signInView.getPromptLabel().setVisible(true);
            }else{
                signInView.getPromptLabel().setVisible(false);//hide error prompt if all is good
            }

            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/landing_page.fxml"));
                root = fxmlLoader.load();

                new LandingPageController(fxmlLoader.getController());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch(IOException e){
                e.printStackTrace();
            }


        });

        this.signInPageView.setActionSignInButton((ActionEvent event) -> {

            /** Idea: after pressing SignIn button, store textfield data onto variables and sent onto
             * an authenticator method inside the SignInPageModel. Retrieve server response if success or not.
             * Send the server response and the event onto a server response evaluator that will act accordingly
             * For example, if the server response retrieved were "Success", the proceed to the try-catch block below.
             * If not, notify the user through the GUI*/

            //store field and dropdown contents
            String userID = signInView.getIDField().getText();
            String pass = signInView.getPassField().getText();
            String userType = signInView.getUserTypeBox().getValue();

            if(userID.isEmpty() || pass.isEmpty() || userType == null) {
                signInView.getPromptLabel().setText("Please accomplish all fields.");
                signInView.getPromptLabel().setVisible(true);
            }else{

                signInView.getPromptLabel().setVisible(false);//hide error prompt if all is good

                //included try-catch block here to visualize flow
                try{
                    //opening the main menu should i think be a new thread in itself
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/client_main_menu_page.fxml"));
                    root = fxmlLoader.load();
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

// private void evalServerResponse () {}



}
