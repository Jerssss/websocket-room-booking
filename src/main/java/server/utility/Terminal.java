package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Terminal {
    private StringProperty terminalId;
    private StringProperty terminalRoom;
    private StringProperty terminalOs;
    private StringProperty terminalStatus;
    private StringProperty startTime;
    private StringProperty endTime;

    public Terminal(String terminalId, String terminalRoom, String terminalOS, String terminalStatus, String startTime, String endTime) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalOs = new SimpleStringProperty(terminalOS);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }

    // Getters and setters with StringProperty
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

    public String getTerminalOs() {
        return terminalOs.get();
    }

    public StringProperty terminalOsProperty() {
        return terminalOs;
    }

    public String getTerminalStatus() {
        return terminalStatus.get();
    }

    public StringProperty terminalStatusProperty() {
        return terminalStatus;
    }

    public void setTerminalStatus(String terminalStatus) {
        this.terminalStatus.set(terminalStatus);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }
    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }
}
