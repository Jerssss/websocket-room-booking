package client.model;

import references.User;
import references.Equipment;
import references.Hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientModel {

    private HashMap<String, Hardware> hardwareMenu;//retrieve from the server



    private User user; //retrieve from the server

    private final List<Equipment> cart;

    //initial setting of fields as null
    public ClientModel () {
        this.user = null;
        this.hardwareMenu = null;
        this.cart = null;
    }

    public ClientModel (User user, HashMap<String, Hardware> hardwareMenu, List<Equipment> cart) {
        this.user = user;
        this.hardwareMenu = hardwareMenu;
        this.cart = new ArrayList<>();
    }

    //TODO
//    public Borrow requestsToBorrow() {
//
//    }


}
