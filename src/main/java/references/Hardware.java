package references;

public class Hardware extends Equipment{

    private int qty;
    //feels like we need more data members here?


    //constructors
    public Hardware (int id, String name, String description, int totalQuantity, int available ) {
        super(id, name, description, totalQuantity, available);
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