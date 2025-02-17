package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private final SimpleStringProperty reservationId;
    private final SimpleStringProperty userId;
    private final SimpleStringProperty roomId;
    private final SimpleStringProperty terminalId;
    private final SimpleStringProperty reservationDate;
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleStringProperty status;

    // Constructor
    public Reservation(String reservationId, String userId, String roomId, String terminalId, String reservationDate,
                       String startTime, String endTime, String status) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.roomId = new SimpleStringProperty(roomId);
        this.terminalId = new SimpleStringProperty(terminalId);
        this.reservationDate = new SimpleStringProperty(reservationDate);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
    }

    // Property methods for JavaFX
    public StringProperty reservationIdProperty() {
        return reservationId;
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public StringProperty terminalIdProperty() {
        return terminalId;
    }

    public StringProperty reservationDateProperty() {
        return reservationDate;
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

    // Getters and Setters
    public String getReservationId() {
        return reservationId.get();
    }

    public void setReservationId(String reservationId) {
        this.reservationId.set(reservationId);
    }

    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getTerminalId() {
        return terminalId.get();
    }

    public void setTerminalId(String terminalId) {
        this.terminalId.set(terminalId);
    }

    public String getReservationDate() {
        return reservationDate.get();
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate.set(reservationDate);
    }

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
