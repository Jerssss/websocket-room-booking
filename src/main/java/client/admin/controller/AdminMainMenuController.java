package client.admin.controller;

import client.admin.model.AddNewTerminalModel;
import client.admin.model.AdminMainMenuModel;
import client.admin.view.AddNewTerminalView;
import client.admin.view.AdminMainMenuView;


public class AdminMainMenuController {
    private final AdminMainMenuView adminMainMenuView;
    private final AdminMainMenuModel adminMainMenuModel;

    public AdminMainMenuController(client.admin.view.AdminMainMenuView adminMainMenuView, client.admin.model.AdminMainMenuModel adminMainMenuModel) {
        this.adminMainMenuView = adminMainMenuView;
        this.adminMainMenuModel = adminMainMenuModel;
    }
}
