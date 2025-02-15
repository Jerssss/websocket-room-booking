// File: server/utility/ApprovalTerminal.java
package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApprovalTerminal {
    private final StringProperty terminalId;
    private final StringProperty terminalRoom;
    private final StringProperty terminalStatus;
    private final StringProperty reservationId;
    private final StringProperty userId;
    private final StringProperty reservationDate;

    // Full Constructor
    public ApprovalTerminal(String terminalId, String terminalRoom, String terminalStatus,
                            String reservationId, String userId, String reservationDate) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.reservationDate = new SimpleStringProperty(reservationDate);
    }

    // Getters and Property Methods for TableView Bindings
    public String getTerminalId() {
        return terminalId.get();
    }

    public StringProperty terminalIdProperty() {
        return terminalId;
    }

    public String getTerminalRoom() {
        return terminalRoom.get();
    }

    public StringProperty terminalRoomProperty() {
        return terminalRoom;
    }

    public String getTerminalStatus() {
        return terminalStatus.get();
    }

    public StringProperty terminalStatusProperty() {
        return terminalStatus;
    }

    public String getReservationId() {
        return reservationId.get();
    }

    public StringProperty reservationIdProperty() {
        return reservationId;
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public String getReservationDate() {
        return reservationDate.get();
    }

    public StringProperty reservationDateProperty() {
        return reservationDate;
    }

    public void setTerminalStatus(String approved) {
    }
}
