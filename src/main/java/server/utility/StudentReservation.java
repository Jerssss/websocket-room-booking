package server.utility;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class StudentReservation {
    private final StringProperty reservationId;
    private StringProperty terminalId;
    private StringProperty terminalRoom;
    private StringProperty date;
    private StringProperty terminalStatus;


    // Full Constructor
    public StudentReservation(String reservationId, String terminalId, String terminalRoom, String date, String terminalStatus) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.date = new SimpleStringProperty(date); // Default value since XML has no <date>
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
    }


    public StudentReservation(String reservationId, String terminalRoom, String terminalStatus) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);


    }




    // Getters and Property Methods for TableView Bindings
    public String getReservationId() {
        return reservationId.get();
    }


    public StringProperty reservationIdProperty() {
        return reservationId;
    }


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


    public String getDate() {
        return date.get();
    }


    public StringProperty dateProperty() {
        return date;
    }


    public String getTerminalStatus() {
        return terminalStatus.get();
    }


    public StringProperty terminalStatusProperty() {
        return terminalStatus;
    }
}
