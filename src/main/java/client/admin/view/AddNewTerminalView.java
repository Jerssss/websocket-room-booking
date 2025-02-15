package client.admin.view;

import client.admin.model.AddNewTerminalModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.admin.controller.AddNewTerminalController;
import server.admin.AddNewTerminalProcessor;

import java.io.IOException;


public class AddNewTerminalView {
    private AddNewTerminalController controller;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Button redirectAddTerminalWindowButton;

    @FXML
    private TextField terminalNoTextField;

    @FXML
    private TextField roomTextField;

    @FXML
    private TextField osTypeTextField;

    @FXML
    private TextField statusTextField;
    @FXML
    private ComboBox dayComboBox;
    @FXML
    private ComboBox timeComboBox;
    private String terminalId;
    private String room;
    private String osType;
    private String status;

    // Getters for UI fields
    public TextField getTerminalNoTextField() {
        return terminalNoTextField;
    }

    public TextField getRoomTextField() {
        return roomTextField;
    }

    public TextField getOsTypeTextField() {
        return osTypeTextField;
    }

    public TextField getStatusTextField() {
        return statusTextField;
    }
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Setters for button actions
    public void setSaveChangesButtonAction(EventHandler<ActionEvent> handler) {
        saveChangesButton.setOnAction(handler);
    }
    private void redirectAddTerminalWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_terminal_window.fxml"));
            Parent root = loader.load();
            AddNewTerminalView view = loader.getController();
            AddNewTerminalController controller = new AddNewTerminalController(view);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setController(AddNewTerminalController controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        if (redirectAddTerminalWindowButton != null) {
            redirectAddTerminalWindowButton.setOnAction(event -> redirectAddTerminalWindow(event)
            );
        }
    }
}