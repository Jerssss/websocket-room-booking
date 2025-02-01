package client.controller.landingpage;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import client.view.clientview.LandingPageView;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class LandingPageController {

    private FXMLLoader fxmlLoader;
    private Parent parent;

    @FXML
    private Button myButton;
    @FXML
    private void addHoverEffect(MouseEvent event) {
        myButton.setEffect(new DropShadow());

    }
    @FXML
    private void removeHoverEffect(MouseEvent event) {
        myButton.setEffect(null);
    }

    @FXML
    private void handleButtonClick() {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), myButton);
        fade.setFromValue(1.0);
        fade.setToValue(0.5);
        fade.setAutoReverse(true);
        fade.setCycleCount(2);
        fade.play();
    }


    public LandingPageController(LandingPageView view){
        //TODO: landing page logic
    }


}
