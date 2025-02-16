package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LogReport {
    private final StringProperty userID;
    private final StringProperty userType;
    private final StringProperty roomNumber;
    private final StringProperty status;
    private final StringProperty date;

    public LogReport(String user, String uType, String room, String stat, String date) {
        this.userID = new SimpleStringProperty(user);
        this.userType = new SimpleStringProperty(uType);
        this.roomNumber = new SimpleStringProperty(room);
        this.status = new SimpleStringProperty(stat);
        this.date = new SimpleStringProperty(date);
    }

    // Getters for the actual values
    public String getUserID() {
        return userID.get();
    }

    public String getUserType() {
        return userType.get();
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getDate() {
        return date.get();
    }

    // Property getters (already present)
    public StringProperty userIDProperty() {
        return userID;
    }

    public StringProperty userTypeProperty() {
        return userType;
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty dateProperty() {
        return date;
    }
}
