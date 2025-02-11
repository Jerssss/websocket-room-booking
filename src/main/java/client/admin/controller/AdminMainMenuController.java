package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import client.admin.model.AdminMainMenuModel;
import client.admin.view.AddNewTerminalView;
import client.admin.view.AdminMainMenuView;
import client.admin.model.AddNewTerminalModel;
import client.admin.view.AddNewTerminalView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminMainMenuController {

    private final AdminMainMenuView view;
    private final AdminMainMenuModel model;

    public AdminMainMenuController(AdminMainMenuView view, AdminMainMenuModel model) {
        this.view = view;
        this.model = model;

        // Set up button actions
        this.view.setActionAddNewTerminalButton(event -> handleAddNewTerminal());
    }

    private void handleAddNewTerminal() {
        System.out.println("Navigating to Add New Terminal...");
    }

}
