package client.controller.landingpageController;


import client.model.mainmenuModel.SignInPageModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class LandingPageController {

    private FXMLLoader fxmlLoader; //loads the fxml file
    private Parent root; //stores the fxml root node of the fxml file loaded (in this case, a BorderPane object is the root node of our fxml files
    private SignInPageController signInPageController;

    public LandingPageController(LandingPageView view){
        view.setActionSignInButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/signin_page.fxml"));
                root = fxmlLoader.load(); //saves loaded fxml file's root node into the object root
                //creates a signin page controller object with the fxml file as its interface, whilst creating a new signin page model
                signInPageController = new SignInPageController(fxmlLoader.getController(), new SignInPageModel());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //sets the stage or window to display the scene
                Scene scene = new Scene(root); //a new scene or window is created with the root node (which contains all the elements in the fxml file)
                stage.setScene(scene);
                stage.show();//display
            }catch(IOException e){
                e.printStackTrace();
            }
        });

        view.setActionSignUpButton((ActionEvent event) -> {
            //store field and dropdown contents
            String userID = view.getIDField().getText();
            String pass = view.getPassField().getText();
            String userType = view.getUserTypeBox().getValue();

            if(userID.isEmpty() || pass.isEmpty() || userType == null) {
                view.getPromptLabel().setText("Please accomplish all fields.");
                view.getPromptLabel().setVisible(true);
            }else{
                view.getPromptLabel().setVisible(false);//hide error prompt if all is good

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
            }
        });

    }


}
