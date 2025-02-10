module Lendify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.logging;
    requires java.xml;

    exports client;

    opens client.landingpage to javafx.fxml;

    opens client.signup to javafx.fxml;

    opens client.login to javafx.fxml;

    opens client to javafx.fxml;

    opens client.student.view to javafx.fxml;
}