package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private SimpleStringProperty reservationId;
    private SimpleStringProperty userId;
    private final SimpleStringProperty terminalNumber;
    private final SimpleStringProperty roomNumber;
    private final SimpleStringProperty date;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;
    private final SimpleStringProperty status;
    private SimpleStringProperty edit;

    // Constructor
    public Reservation(String reservationId, String userId, String terminalNumber, String roomNumber, String date,
                       String startTime, String endTime, String status, String edit) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.terminalNumber = new SimpleStringProperty(terminalNumber);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
        this.edit = new SimpleStringProperty(edit);
    }

    public Reservation(String reservationId, String userId, String terminalNumber, String roomNumber, String date,
                       String startTime, String endTime, String status) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.terminalNumber = new SimpleStringProperty(terminalNumber);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
    }

    // String Properties
    public StringProperty reservationIdProperty() {
        return reservationId;
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public StringProperty terminalNumberProperty() {
        return terminalNumber;
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty editProperty() {
        return edit;
    }

    // Getters and Setters
    public String getReservationId() {return reservationId.get();}

    public void setReservationId(String reservationId) {this.reservationId.set(reservationId);}

    public String getUserId() {return userId.get();}

    public void setUserId(String userId) {this.userId.set(userId);}

    public String getTerminalNumber() {return terminalNumber.get();}

    public String getRoomNumber() {return roomNumber.get();}

    public String getDate() {return date.get();}

    public void setDate(String date) {this.date.set(date);}

    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String reservationStatus) {
        this.status.set(reservationStatus);
    }
}
