package client.student.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private final StringProperty reservationID;
    private final StringProperty userID;
    private final StringProperty terminalID;
    private final StringProperty terminalRoom;
    private final StringProperty terminalStatus;

    public Reservation(String reservationID, String userID, String terminalID, String terminalRoom, String terminalStatus) {
        this.reservationID = new SimpleStringProperty(reservationID);
        this.userID = new SimpleStringProperty(userID);
        this.terminalID = new SimpleStringProperty(terminalID);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
    }

    public StringProperty reservationIDProperty() {
        return reservationID;
    }

    public StringProperty userIDProperty() {
        return userID;
    }

    public StringProperty terminalIDProperty() {
        return terminalID;
    }

    public StringProperty terminalRoomProperty() {
        return terminalRoom;
    }

    public StringProperty terminalStatusProperty() {
        return terminalStatus;
    }
}
