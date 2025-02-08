package references;

import java.io.Serializable;
import java.util.List;

public class Borrow implements Serializable {

    private User user;
    private List<Equipment> borrowedEquipment;
    private boolean status; //if returned or pending return ?

    //used client side
    public Borrow (User user, List<Equipment> borrowedEquipment, boolean status){
        this.user = user;
        this.borrowedEquipment = borrowedEquipment;
        this.status = status;
    }

    //i dont know if we need more constructors here


    public User getUser() {
        return user;
    }

    public List<Equipment> getBorrowedEquipment() {
        return borrowedEquipment;
    }

    public boolean isStatus() {
        return status;
    }

    //setters
    public void setUser(User user) {
        this.user = user;
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
