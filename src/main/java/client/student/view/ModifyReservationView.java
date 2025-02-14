package client.student.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import server.utility.Terminal;

public class ModifyReservationView {

    @FXML
    private VBox centerPane;
    @FXML
    private Button searchButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    private TextField searchStudResTextField;

    @FXML
    private TableView<Terminal> modResTableView;
    @FXML
    private TableColumn<Terminal, String> roomNumberColumn;
    @FXML
    private TableColumn<Terminal, String> terminalColumn;
    @FXML
    private TableColumn<Terminal, String> terminalOSColumn;
    @FXML
    private TableColumn<Terminal, String> terminalStatusColumn;
    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();


    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }

    public void setActionSaveChangesButton(EventHandler<ActionEvent> event) {
        saveChangesButton.setOnAction(event);
    }

    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }
}
