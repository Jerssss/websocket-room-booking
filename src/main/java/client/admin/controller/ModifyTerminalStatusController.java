package client.admin.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import client.admin.view.ModifyTerminalStatusView;

public class ModifyTerminalStatusController {

    private ModifyTerminalStatusView view;

    // Constructor to set the view (in case you need it to access view methods)
    public ModifyTerminalStatusController(ModifyTerminalStatusView view) {
        this.view = view;
    }

    // Set the action for the Search button
    public void handleSearchButtonAction() {
        view.setActionSearchButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Add the logic for search button click
                System.out.println("Search button clicked!");
            }
        });
    }

    // Set the action for the Save Changes button
    public void handleSaveChangesButtonAction() {
        view.setActionSaveChangesButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Add the logic for save changes button click
                System.out.println("Save Changes button clicked!");
            }
        });
    }

}
