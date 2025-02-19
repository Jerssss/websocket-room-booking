package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private SimpleStringProperty reservationId;
    private SimpleStringProperty userId;
    private  SimpleStringProperty terminalNumber;
    private  SimpleStringProperty roomNumber;
    private  SimpleStringProperty date;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;
    private  SimpleStringProperty status;
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

    public Reservation(Reservation other) {
        this.reservationId = new SimpleStringProperty(other.getReservationId());
        this.userId = new SimpleStringProperty(other.getUserId());
        this.terminalNumber = new SimpleStringProperty(other.getTerminalNumber());
        this.roomNumber = new SimpleStringProperty(other.getRoomNumber());
        this.date = new SimpleStringProperty(other.getDate());
        this.startTime = new SimpleStringProperty(other.getStartTime());
        this.endTime = new SimpleStringProperty(other.getEndTime());
        this.status = new SimpleStringProperty(other.getStatus());
    }

    public Reservation(String reservationId, String userId) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        // Initialize all other properties with empty values
        this.terminalNumber = new SimpleStringProperty("");
        this.roomNumber = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
        this.startTime = new SimpleStringProperty("");
        this.endTime = new SimpleStringProperty("");
        this.status = new SimpleStringProperty("");
    }

    // String Properties
    public StringProperty reservationIdProperty() {
        return reservationId;
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

    // Getters and Setters
    public String getReservationId() {return reservationId.get();}

    public void setReservationId(String reservationId) {this.reservationId.set(reservationId);}

    public String getUserId() {return userId.get();}

    public void setUserId(String userId) {this.userId.set(userId);}

    public String getTerminalNumber() {return terminalNumber.get();}

    public void setTerminalNumber(String terminalNumber) {this.terminalNumber.set(terminalNumber);}

    public String getRoomNumber() {return roomNumber.get();}

    public void setRoomNumber(String roomNumber) {this.roomNumber.set(roomNumber);}

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
