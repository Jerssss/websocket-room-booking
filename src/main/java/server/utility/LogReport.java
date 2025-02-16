package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LogReport {
    private final StringProperty userID;
    private final StringProperty userType;
    private final StringProperty action;
    private final StringProperty date;
    private final StringProperty time;

    public LogReport(String userID, String userType, String action, String date, String time) {
        this.userID = new SimpleStringProperty(userID);
        this.userType = new SimpleStringProperty(userType);
        this.action = new SimpleStringProperty(action);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }



    // Return actual string values
    public String getUserID() {
        return userID.get();
    }

    public String getUserType() {
        return userType.get();
    }

    public String getAction() {
        return action.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    // Property getters (JavaFX Table binding)
    public StringProperty userIDProperty() {
        return userID;
    }

    public StringProperty userTypeProperty() {
        return userType;
    }

    public StringProperty actionProperty() {
        return action;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty timeProperty() {
        return time;
    }


}
