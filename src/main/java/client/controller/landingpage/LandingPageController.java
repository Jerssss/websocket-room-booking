package client.controller.landingpage;


import client.controller.signin.SignInPageController;
import client.model.scenemodels.SignInPageModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import client.view.clientview.LandingPageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class LandingPageController {

    private FXMLLoader fxmlLoader;
    private Parent root;
    private SignInPageController signInPageController;

    public LandingPageController(LandingPageView view){
        view.setActionSignInButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/signin_page.fxml"));
                root = fxmlLoader.load();

                signInPageController = new SignInPageController(fxmlLoader.getController(), new SignInPageModel());

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
