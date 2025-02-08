package references;

import java.io.Serializable;

public class Terminal implements Serializable {

    private int terminalId;
    private String terminalType; // mac, windows, linux
    private int terminalRoom;
    private String terminalRoomType; // Open Lab, Classroom
    private String terminalStatus; // Available, Reserved, Maintenance, Down

    public Terminal(int id, String type, int room, String roomType, String status) {
        this.terminalId = id;
        this.terminalType = type;
        this.terminalRoom = room;
        this.terminalRoomType = roomType;
        this.terminalStatus = status;
    }

    // Getters and Setters
    public int getTerminalId() { return terminalId; }
    public String getTerminalType() { return terminalType; }
    public int getTerminalRoom() { return terminalRoom; }
    public String getTerminalRoomType() { return terminalRoomType; }
    public String getTerminalStatus() { return terminalStatus; }

    public void setTerminalId(int id){
        this.terminalId = id;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public void setTerminalRoom(int terminalRoom) {
        this.terminalRoom = terminalRoom;
    }

    public void setTerminalRoomType(String terminalRoomType) {
        this.terminalRoomType = terminalRoomType;
    }

    public void setTerminalStatus(String status) {
        this.terminalStatus = status;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "id=" + terminalId +
                ", type=" + terminalType +
                ", room=" + terminalRoom +
                ", roomType='" + terminalRoomType + '\'' +
                ", status=" + terminalStatus +
                '}';
    }
}
