package references;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {

    private String name;
    private String studID;
    private String username;
    private String password;
    private List<Borrow> borrowHistory;

    //constructor for customer objects
    public Student(String name, String studID, String username, String password) {
        this.name = name;
        this.studID = studID;
        this.username = username;
        this.password = password;
    }

    //constructor for customer login where the object is already saved in the system
    public Student (Student student) {
        this.name = student.getName();
        this.studID = student.getStudID();
        this.username = student.getUsername();
        this.password = student.getPassword();
        this.borrowHistory = new ArrayList<>();
    }

    //

    //getters
    public String getName() {
        return name;
    }
    public String getStudID() {
        return studID;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public List<Borrow> getBorrowHistory() {
        return borrowHistory;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setStudID(String studID) {
        this.studID = studID;
    }
    public void setUsername(String userName) {
        this.username = userName;
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
                "name = " + name + '\'' +
                ", username='" + username + '\'' +
                ", studentID='" + studID + '\'' +
                ", password='" + password + '\'' +
                ", borrowHistory=" + borrowHistory +
                '}';
    }
}
