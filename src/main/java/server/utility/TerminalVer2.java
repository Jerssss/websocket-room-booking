package server.utility;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TerminalVer2 {
    private StringProperty terminalId;
    private StringProperty terminalRoom;
    private StringProperty terminalOS;
    private StringProperty terminalStatus;
    private SimpleStringProperty date;
    private SimpleStringProperty time;

    public TerminalVer2(String terminalId, String terminalRoom, String terminalOS, String terminalStatus, String date, String time) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalOS = new SimpleStringProperty(terminalOS);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
        this.date = new SimpleStringProperty(date);
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

    public String getTerminalOS() {
        return terminalOS.get();
    }

    public void setTerminalOS(String terminalOs) {
        this.terminalOS.set(terminalOs);
    }

    public StringProperty terminalOSProperty() {
        return terminalOS;
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
    public void setDate(String date) {
        this.date.set(date);
    }
    public StringProperty dateProperty() {
        return date;
    }
    public void setTime(String time) {
        this.time.set(time);
    }
    public StringProperty timeProperty() {
        return time;
    }
}
