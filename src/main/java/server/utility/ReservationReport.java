package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReservationReport {
    private final StringProperty reservationId;
    private final StringProperty userId;
    private final StringProperty terminalId;
    private final StringProperty roomNumber;
    private final StringProperty status;
    private final StringProperty date;
    private final StringProperty startTime;
    private final StringProperty endTime;

    public ReservationReport(String reservationId, String userId, String terminalId,
                             String roomNumber, String status, String date,
                             String startTime, String endTime) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.terminalId = new SimpleStringProperty(terminalId);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }

    // Return actual string values
    public String getReservationId() {
        return reservationId.get();
    }

    public String getUserId() {
        return userId.get();
    }

    public String getTerminalId() {
        return terminalId.get();
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

    public String getStartTime() {
        return startTime.get();
    }

    public String getEndTime() {
        return endTime.get();
    }

    // Property getters (JavaFX Table binding)
    public StringProperty reservationIdProperty() {
        return reservationId;
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public StringProperty terminalIdProperty() {
        return terminalId;
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

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }
}
