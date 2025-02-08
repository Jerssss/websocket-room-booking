package client;

import client.landingpage.LandingPageController;

public class ClientController {

    public ClientController (ClientView view){
        System.out.println("Loading client's landing page controller...");
        new LandingPageController(view.getFxmlLoader().getController());
    }
}
