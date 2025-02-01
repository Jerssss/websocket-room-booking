module EquipmentBorrowingApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.xml;
    requires javafx.base;

    opens client to javafx.fxml;
    opens client.view to javafx.fxml;
    opens client.view.clientview to javafx.fxml;
    opens fxml.client to javafx.fxml;
    opens images.client to javafx.fxml; // Corrected opening for images

    exports client;
    exports client.controller;
    exports client.view;
    exports client.view.clientview;
}
