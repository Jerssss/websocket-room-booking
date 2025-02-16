// File: server/utility/ApprovalReservation.java
package server.utility;

import javafx.beans.property.*;

public class ApprovalReservation {
    private final StringProperty reservationId;
    private final StringProperty userId;
    private final StringProperty terminalId;
    private final StringProperty roomId;
    private final StringProperty reservationDate;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty status;

    public ApprovalReservation(String reservationId, String userId, String terminalId, String roomId,
                               String reservationDate, String startTime, String endTime, String status) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.terminalId = new SimpleStringProperty(terminalId);
        this.roomId = new SimpleStringProperty(roomId);
        this.reservationDate = new SimpleStringProperty(reservationDate);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
    }

    public String getReservationId() { return reservationId.get(); }
    public StringProperty reservationIdProperty() { return reservationId; }

    public String getUserId() { return userId.get(); }
    public StringProperty userIdProperty() { return userId; }

    public String getTerminalId() { return terminalId.get(); }
    public StringProperty terminalIdProperty() { return terminalId; }

    public String getRoomId() { return roomId.get(); }
    public StringProperty roomIdProperty() { return roomId; }

    public String getReservationDate() { return reservationDate.get(); }
    public StringProperty reservationDateProperty() { return reservationDate; }

    public String getStartTime() { return startTime.get(); }
    public StringProperty startTimeProperty() { return startTime; }

    public String getEndTime() { return endTime.get(); }
    public StringProperty endTimeProperty() { return endTime; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}
