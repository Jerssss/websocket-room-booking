package main.client.model;

import main.client.view.clientview.ClientView;
import main.references.Borrow;
import main.references.Equipment;
import main.references.Hardware;
import main.references.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientModel {

    private HashMap<String, Hardware> hardwareMenu;//retrieve from the server

    //TODO: do we need a terminal class pa?

    private Student student; //retrieve from the server

    private final List<Equipment> cart;

    //initial setting of fields as null
    public ClientModel () {
        this.student = null;
        this.hardwareMenu = null;
        this.cart = null;
    }

    public ClientModel (Student student, HashMap<String, Hardware> hardwareMenu, List<Equipment> cart) {
        this.student = student;
        this.hardwareMenu = hardwareMenu;
        this.cart = new ArrayList<>();
    }

    //TODO
//    public Borrow placeRequestToBorrow() {
//
//    }


}
