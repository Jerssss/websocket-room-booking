package server.utility;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class StudentReservation {
    private final StringProperty reservationId;
    private final StringProperty userId;
    private final StringProperty terminalNumber;
    private final StringProperty terminalRoom;
    private final StringProperty date;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty terminalStatus;


    // Full Constructor
    public StudentReservation(String reservationId, String userId, String terminalNumber, String terminalRoom, String date, String startTime, String endTime, String terminalStatus) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userId = new SimpleStringProperty(userId);
        this.terminalNumber = new SimpleStringProperty(terminalNumber);
        this.terminalRoom = new SimpleStringProperty(terminalRoom);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.terminalStatus = new SimpleStringProperty(terminalStatus);
    }




    // Getters and Property Methods for TableView Bindings
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


    public String getTerminalNumber() {
        return terminalNumber.get();
    }


    public StringProperty terminalNumberProperty() {
        return terminalNumber;
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

    public String getTerminalStatus() {
        return terminalStatus.get();
    }

    public StringProperty terminalStatusProperty() {
        return terminalStatus;
    }
}
