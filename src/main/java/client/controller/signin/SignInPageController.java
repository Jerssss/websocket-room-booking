package client.controller.signin;

import client.controller.landingpage.LandingPageController;
import client.model.scenemodels.SignInPageModel;
import client.view.clientview.SignInPageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInPageController {

    private final SignInPageView signInPageView;
    private final SignInPageModel signInPageModel;
    private FXMLLoader fxmlLoader;
    private Parent root;


    public SignInPageController(SignInPageView signInView, SignInPageModel signInModel){
        this.signInPageView = signInView;
        this.signInPageModel = signInModel;

//        button action listener
        this.signInPageView.setActionSignUpButton((ActionEvent event) ->{
            //load the view before getting its controller
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/signin_page.fxml"));
                root = fxmlLoader.load();

                new LandingPageController(fxmlLoader.getController());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}
