package client.admin.model;

import javafx.collections.ObservableList;
import server.admin.ReservationApprovalProcessor;
import server.utility.StudentReservation;

public class ReservationApprovalModel {
    private final ReservationApprovalProcessor processor;

    public ReservationApprovalModel() {
        this.processor = new ReservationApprovalProcessor();
    }

    // Load all reservations using the processor
    public ObservableList<StudentReservation> loadAllReservations() {
        return processor.loadAllReservations();
    }
}
