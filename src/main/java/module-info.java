module Lendify {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.logging;
    requires java.xml;

    opens client to javafx.fxml;
    opens client.view to javafx.fxml;
    opens client.view.clientview to javafx.fxml;

    exports client;
    exports client.controller;
    exports client.view;
    exports client.view.clientview;
}
