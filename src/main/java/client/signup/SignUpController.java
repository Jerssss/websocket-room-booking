package client.signup;

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
    private final SignUpModel signUpModel;


    public SignUpController(SignUpView signUpView, SignUpModel signUpModel) {
        this.signUpView = signUpView;
        this.signUpModel = signUpModel;



        this.signUpView.setActionSignInButton((ActionEvent event) -> {
            try{
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = fxmlLoader.load(); //saves loaded fxml file's root node into the object root

                new LoginController(fxmlLoader.getController(), new LoginModel()); //allows mutation and display

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
            String userType = signUpView.getUserTypeBox().getValue();
            String userID = signUpView.getIDField().getText();
            String name = signUpView.getNameField().getText();
            String courseYear = signUpView.getCourseYearField().getText();
            String type = signUpView.getFacultyTypeField().getText();
            String pass = signUpView.getPassField().getText();


            //prompter when fields are unaccomplished
            if(userID.isEmpty() || pass.isEmpty() || userType == null
                    || name.isEmpty() || courseYear.isEmpty() || type.isEmpty()) {
                signUpView.getPromptLabel().setText("Please accomplish all fields.");
                signUpView.getPromptLabel().setVisible(true);
            }else{
                signUpView.getPromptLabel().setVisible(false);//hide error prompt if all is good

//                try{
//          TODO: include authentication logic or method call for authentication and server response
//                }
            }
        });



    }
}
