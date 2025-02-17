package client.student.view;

import client.admin.view.AdminMainMenuView;
import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import client.signup.SignUpController;
import client.signup.SignUpModel;
import client.student.model.CreateReservationModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateReservationView  {

    // Variables corresponding to the FXML components
    @FXML
    private VBox centerPane;

    @FXML
    private Label roomsLabel;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private TextField monthTextField;

    @FXML
    private TextField dayTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private Label dateLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private ScrollPane roomsScrollPane;

    @FXML
    private GridPane roomGridPane;



    // Getters for the components
    public VBox getCenterPane() {
        return centerPane;
    }

    public Label getRoomsLabel() {
        return roomsLabel;
    }

    public TextField getStartTimeTextField() {
        return startTimeTextField;
    }

    public TextField getEndTimeTextField() {
        return endTimeTextField;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public TextField getMonthTextField() {
        return monthTextField;
    }

    public TextField getDayTextField() {
        return dayTextField;
    }

    public TextField getYearTextField() {
        return yearTextField;
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public Button getRefreshButton() {

        return refreshButton;
    }

    public ScrollPane getRoomsScrollPane() {
        return roomsScrollPane;
    }

    public GridPane getRoomGridPane() {
        return roomGridPane;
    }
}