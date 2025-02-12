module Lendify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.logging;
    requires java.xml;
    requires java.desktop;

    exports client;
    exports client.utility;

    opens client.landingpage to javafx.fxml;
    opens client.signup to javafx.fxml;
    opens client.login to javafx.fxml;
    opens client to javafx.fxml;
    opens client.student.view to javafx.fxml;
    opens client.admin.view to javafx.fxml;
    opens client.admin.controller to javafx.fxml;
}
