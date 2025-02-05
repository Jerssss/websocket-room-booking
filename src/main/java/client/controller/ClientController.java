package client.controller;

import client.controller.landingpageController.LandingPageController;
import client.view.ClientView;

public class ClientController {
    public ClientController(ClientView view) {
        System.out.println("Successfully loaded landing page controller.");
        new LandingPageController(view.getFxmlLoader().getController());
    }
}
