package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApprovalReservation {

    private final StringProperty reservationId;
    private final StringProperty userId;
    private final StringProperty terminalId;
    private final StringProperty roomNumber;
    private final StringProperty reservationDate;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty status;

    public ApprovalReservation(String reservationId, String userId, String terminalId,
                               String roomNumber, String reservationDate,
                               String startTime, String endTime, String status) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.terminalId = new SimpleStringProperty(terminalId);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.reservationDate = new SimpleStringProperty(reservationDate);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
    }

    // Getters for JavaFX TableView
    public StringProperty reservationIdProperty() { return reservationId; }
    public StringProperty userIdProperty() { return userId; }
    public StringProperty terminalIdProperty() { return terminalId; }
    public StringProperty roomNumberProperty() { return roomNumber; }
    public StringProperty reservationDateProperty() { return reservationDate; }
    public StringProperty startTimeProperty() { return startTime; }
    public StringProperty endTimeProperty() { return endTime; }
    public StringProperty statusProperty() { return status; }

    // Optional: Standard Getters for Backend Use
    public String getReservationId() { return reservationId.get(); }
    public String getUserId() { return userId.get(); }
    public String getTerminalId() { return terminalId.get(); }
    public String getRoomNumber() { return roomNumber.get(); }
    public String getReservationDate() { return reservationDate.get(); }
    public String getStartTime() { return startTime.get(); }
    public String getEndTime() { return endTime.get(); }
    public String getStatus() { return status.get(); }
}
