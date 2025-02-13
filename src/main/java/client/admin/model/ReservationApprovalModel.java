package client.admin.model;

public class ReservationApprovalModel {
    private int id; 
    private String studentName;
    private String date;
    private String status;

    public ReservationApprovalModel(int id, String studentName, String date) {
        this.id = id;
        this.studentName = studentName;
        this.date = date;
        this.status = "Pending";
    }

    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getDate() { return date; }
    public String getStatus() { return status; }

    public void approve() { this.status = "Approved"; }
    public void reject() { this.status = "Rejected"; }
}
