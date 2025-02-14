package client.student.model;

import javafx.beans.property.SimpleStringProperty;

// Reservation Model Class
public class Reservation {
    private final SimpleStringProperty reservationID;
    private final SimpleStringProperty userID;
    private final SimpleStringProperty terminalID;
    private final SimpleStringProperty reservationDate;
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleStringProperty status;

    public Reservation(String reservationID, String userID, String terminalID, String reservationDate, String startTime, String endTime, String status) {
        this.reservationID = new SimpleStringProperty(reservationID);
        this.userID = new SimpleStringProperty(userID);
        this.terminalID = new SimpleStringProperty(terminalID);
        this.reservationDate = new SimpleStringProperty(reservationDate);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
    }

    // Getters and property methods
    public String getReservationID() { return reservationID.get(); }
    public SimpleStringProperty reservationIDProperty() { return reservationID; }

    public String getUserID() { return userID.get(); }
    public SimpleStringProperty userIDProperty() { return userID; }

    public String getTerminalID() { return terminalID.get(); }
    public SimpleStringProperty terminalIDProperty() { return terminalID; }

    public String getReservationDate() { return reservationDate.get(); }
    public SimpleStringProperty reservationDateProperty() { return reservationDate; }

    public String getStartTime() { return startTime.get(); }
    public SimpleStringProperty startTimeProperty() { return startTime; }

    public String getEndTime() { return endTime.get(); }
    public SimpleStringProperty endTimeProperty() { return endTime; }

    public String getStatus() { return status.get(); }
    public SimpleStringProperty statusProperty() { return status; }
}