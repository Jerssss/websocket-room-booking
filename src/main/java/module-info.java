module Lendify {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.logging;
    requires java.xml;

    opens client to javafx.fxml;
//
    exports client;

}
