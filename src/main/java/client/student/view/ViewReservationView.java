package client.student.view;

import client.student.controller.ViewReservationController;
import client.utility.SessionManager;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import server.utility.Reservation;
import javafx.fxml.FXML;
import server.student.ViewReservationProcessor;
import client.utility.ServerConnectionManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ViewReservationView {

    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<Reservation> modResTableView;

    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;
    @FXML
    private TableColumn<Reservation, String> userIDColumn;
    @FXML
    private TableColumn<Reservation, String> terminalIDColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, String> startTimeColumn;
    @FXML
    private TableColumn<Reservation, String> endTimeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();
    public TableView<Reservation> getStudResTableView() {
        return modResTableView;
    }


}
