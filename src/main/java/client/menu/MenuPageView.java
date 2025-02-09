package client.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;


public class MenuPageView {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button createReservationButton;
    @FXML
    private Button viewReservationButton;
    @FXML
    private Button modifyReservationButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Label headerDateLabel;
    @FXML
    private Label headerTimeLabel;
    @FXML
    private Label headerNameLabel; //name for the header??
    @FXML
    private Label greetingLabel;
    @FXML
    private Label nameGreetingLabel; //changes the name in the greetings

    public void setActionCreateReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event);
    }

    public void setActionViewReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event);
    }

    public void setActionModifyReservationButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event);
    }

    public void setActionLogOutButton(EventHandler<ActionEvent> event) {
        createReservationButton.setOnAction(event);
    }

    public void setDateLabel(String value){
        headerDateLabel.setText(value); //sets the date when called
    }

    public void setTimeLabel(String value) {
        headerTimeLabel.setText(value);
    }

    public void setHeaderNameLabel(String value) {
        headerNameLabel.setText(value);
    }

    public Button getLogOutButton() {
        return logOutButton;
    }
}
