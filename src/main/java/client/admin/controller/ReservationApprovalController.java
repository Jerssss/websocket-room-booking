public class ReservationApprovalController {

    private ReservationApprovalModel reservation;
    private ReservationApprovalView view;

    public ReservationApprovalController(ReservationApprovalModel reservation) {
        this.reservation = reservation;
        this.view = new ReservationApprovalView(this, reservation);
    }

    public void approveReservation() {
        reservation.approve();
    }

    public void rejectReservation() {
        reservation.reject();
    }

    public static void main(String[] args) {
        ReservationApprovalModel reservation = new ReservationApprovalModel(1, "Bryant Pangilinan", "2025-02-13");
        new ReservationApprovalController(reservation);
    }
}