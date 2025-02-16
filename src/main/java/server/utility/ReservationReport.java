package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReservationReport {
    private final StringProperty reservationId;
    private final StringProperty terminalId;
    private final StringProperty roomNumber;
    private final StringProperty status;
    private final StringProperty date;

    public ReservationReport(String reservationId, String terminalId, String roomNumber, String status, String date) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.terminalId = new SimpleStringProperty(terminalId);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
    }

    public String getReservationId() {
        return reservationId.get();
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

    // Property getters
    public StringProperty reservationIdProperty() {
        return reservationId;
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
}
