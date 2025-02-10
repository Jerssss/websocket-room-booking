package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import client.admin.view.AddNewTerminalView;
import client.admin.view.AdminMainMenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AddNewTerminalController {
    private final AddNewTerminalView addNewTerminalView;
    private final AddNewTerminalModel addNewTerminalModel;

    public AddNewTerminalController(AddNewTerminalView addNewTerminalView, AddNewTerminalModel addNewTerminalModel) {
        this.addNewTerminalView = addNewTerminalView;
        this.addNewTerminalModel = addNewTerminalModel;
    }
}
