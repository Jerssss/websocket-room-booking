package client.student.controller;

import client.student.model.ViewReservationModel;
import client.student.view.ViewReservationView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class ViewReservationController {
    private final ViewReservationView view;
    private final ViewReservationModel model;

    public ViewReservationController(ViewReservationView view, ViewReservationModel model) {
        this.view = view;
        this.model = model;
    }

}