package references;

import java.io.Serializable;

public class Equipment implements Serializable {

    private String name;
    private String description;
    private char type; //whether equipment leased is a terminal or hardware
    private int amountBorrowed;

    //constructor to initialize direct descendants of the Equipment class
    public Equipment (String name, String description, char type, int amtBorrowed) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.amountBorrowed = amtBorrowed;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public char getType() {
        return type;
    }

    public int getAmountBorrowed() {
        return amountBorrowed;
    }

    //setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(char type) {
        this.type = type;
    }

    public void setAmountBorrowed(int amountBorrowed) {
        this.amountBorrowed = amountBorrowed;
    }
}
