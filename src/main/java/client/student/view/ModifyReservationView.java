package client.student.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ModifyReservationView {

    @FXML
    private VBox centerPane;
    @FXML
    private Label modifyLabel;
    @FXML
    private Label timePickerLabel;
    @FXML
    private Label colonLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField dayTextField;
    @FXML
    private TextField monthTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private Button refreshButton; //might rename to avoid confusion
    @FXML
    private ScrollPane modifyReservationScrollPane;
    @FXML
    private AnchorPane modifyReservationAnchorPane;

    private FXMLLoader fxmlLoader;
    private Parent root;

    //    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
//        refreshButton.setOnAction(event);
//TODO logic for refresh (update) button
//    }

    //TODO setters and getters
}
