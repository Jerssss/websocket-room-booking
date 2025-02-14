package server.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Terminal {
    private StringProperty terminalId;
    private StringProperty terminalRoom;
    private StringProperty terminalOs;
    private StringProperty terminalStatus;

    public Terminal(String terminalId, String terminalRoom, String terminalOs, String terminalStatus) {
        this.terminalId = new SimpleStringProperty(terminalId);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.terminalOs = new SimpleStringProperty(terminalOs);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
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
}
