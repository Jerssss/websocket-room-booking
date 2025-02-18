package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Terminal {
    private StringProperty terminalId;
    private StringProperty terminalRoom;
    private StringProperty terminalOs;
    private StringProperty terminalStatus;
    private SimpleStringProperty reservationDate;
    private SimpleStringProperty time;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;

    public Terminal(String terminalId, String terminalRoom, String terminalOs, String terminalStatus, String reservationDate, String startTime, String endTime) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalOs = new SimpleStringProperty(terminalOs);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.reservationDate = new SimpleStringProperty(reservationDate);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
    }
    public Terminal(String terminalId, String terminalRoom, String terminalOs, String date, String time) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalOs = new SimpleStringProperty(terminalOs);
        this.reservationDate = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    public Terminal(String terminalId, String terminalRoom, String terminalOS, String terminalStatus, String date, String time) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalOs = new SimpleStringProperty(terminalOS);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
        this.reservationDate = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    // Getters and setters with StringProperty
    public String getTerminalId() {
        return terminalId.get();
    }

    public void setTerminalId(String terminalId) {
        this.terminalId.set(terminalId);
    }

    public StringProperty terminalIdProperty() {
        return terminalId;
    }

    public String getTerminalRoom() {
        return terminalRoom.get();
    }

    public void setTerminalRoom(String terminalRoom) {
        this.terminalRoom.set(terminalRoom);
    }

    public StringProperty terminalRoomProperty() {
        return terminalRoom;
    }

    public String getTerminalOs() {
        return terminalOs.get();
    }

    public void setTerminalOs(String terminalOs) {
        this.terminalOs.set(terminalOs);
    }

    public StringProperty terminalOsProperty() {
        return terminalOs;
    }

    public String getTerminalStatus() {
        return terminalStatus.get();
    }

    public void setTerminalStatus(String terminalStatus) {
        this.terminalStatus.set(terminalStatus);
    }

    public StringProperty terminalStatusProperty() {
        return terminalStatus;
    }
    public void setReservationDate(String reservationDate) {
        this.reservationDate.set(reservationDate);
    }
    public String getReservationDate(){
        return reservationDate.get();
    }
    public StringProperty reservationDateProperty() {
        return reservationDate;
    }
    public void setTime(String time) {
        this.time.set(time);
    }
    public StringProperty timeProperty() {
        return time;
    }
    public void startTime(String startTime) {
        this.startTime(startTime);
    }

    public String getStartTime() {
        return startTime.get();
    }
    public StringProperty startTimeProperty() {
        return startTime;
    }

    public void endTime(String endTime) {
        this.endTime(endTime);
    }

    public String getEndTime() {
        return endTime.get();
    }
    public StringProperty endTimeProperty() {
        return endTime;
    }
}
