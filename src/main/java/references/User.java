package references;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String studID;
    private String password;
    private List<Borrow> borrowHistory;

    //constructor for customer objects
    public User(String studID, String password) {
        this.studID = studID;
        this.password = password;
    }

    //constructor for customer login where the object is already saved in the system
    public User(User user) {
        this.studID = user.getStudID();
        this.password = user.getPassword();
        this.borrowHistory = new ArrayList<>();
    }

    //

    //getters
    public String getStudID() {
        return studID;
    }
    public String getPassword() {
        return password;
    }
    public List<Borrow> getBorrowHistory() {
        return borrowHistory;
    }

    //setters
    public void setStudID(String studID) {
        this.studID = studID;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setBorrowHistory(List<Borrow> borrowHistory) {
        this.borrowHistory = borrowHistory;
    }

    @Override
    public String toString(){
        return "Student{" +
                ", studentID='" + studID + '\'' +
                ", password='" + password + '\'' +
                ", borrowHistory=" + borrowHistory +
                '}';
    }
}
