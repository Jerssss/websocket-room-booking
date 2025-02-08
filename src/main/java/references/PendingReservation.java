package references;

import java.util.List;

public class PendingReservation extends Borrow {
    private String reservationID;
    private String requestDate;

    public PendingReservation(String reservationID, User user, List<Equipment> borrowedEquipment, boolean status, String requestDate) {
        super(user, borrowedEquipment, status);
        this.reservationID = reservationID;
        this.requestDate = requestDate;
    }

    // Getters
    public String getReservationID() {
        return reservationID;
    }

    public String getRequestDate() {
        return requestDate;
    }

    // Setters
    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "PendingReservation{" +
                "reservationID='" + reservationID + '\'' +
                ", requestDate='" + requestDate + '\'' +
                ", student=" + getUser() +
                ", borrowedEquipment=" + getBorrowedEquipment() +
                ", status=" + isStatus() +
                '}';
    }
}

