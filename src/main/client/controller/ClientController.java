package main.client.controller;

import main.client.controller.landingpage.LandingPageController;
import main.client.view.clientview.ClientView;

public class ClientController {
    public ClientController(ClientView view) {
        System.out.println("Successfully loaded landing page controller.");
        new LandingPageController(view.getFxmlLoader().getController());
    }
}
