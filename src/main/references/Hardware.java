package main.references;

public class Hardware extends Equipment{

    private int qty;
    //feels like we need more data members here?


    //constructors
    public Hardware (String name, String description, char type, int amtBorrowed, int qty) {
        super(name, description, type, amtBorrowed);
        this.qty = qty;
    }

    //getters
    public int getQty() {
        return qty;
    }

    //setters
    public void setQty(int qty) {
        this.qty = qty;
    }
}
