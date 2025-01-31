package main.references;

import java.io.Serializable;
import java.util.List;

public class Borrow implements Serializable {

    private Student student;
    private List<Equipment> borrowedEquipment;
    private boolean status; //if returned or pending return ?

    //used client side
    public Borrow (Student student, List<Equipment> borrowedEquipment, boolean status){
        this.student = student;
        this.borrowedEquipment = borrowedEquipment;
        this.status = status;
    }

    //i dont know if we need more constructors here


    public Student getStudent() {
        return student;
    }

    public List<Equipment> getBorrowedEquipment() {
        return borrowedEquipment;
    }

    public boolean isStatus() {
        return status;
    }

    //setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setBorrowedEquipment(List<Equipment> borrowedEquipment) {
        this.borrowedEquipment = borrowedEquipment;
    }

    @Override
    public String toString(){
        return "Borrowed{" +
                ", student=" + borrowedEquipment +
                ", borrowed_equipment=" + borrowedEquipment +
                ", status=" + status +
                '}';
    }
}
