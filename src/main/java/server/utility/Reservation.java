package server.utility;

import javafx.beans.property.SimpleStringProperty;

public class Reservation {
    private final SimpleStringProperty reservationId;
    private final SimpleStringProperty userId;
    private final SimpleStringProperty terminalNumber;
    private final SimpleStringProperty roomNumber;
    private final SimpleStringProperty date;
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleStringProperty status;

    // Constructor
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
