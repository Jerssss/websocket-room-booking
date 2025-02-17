package client.student.controller;

import client.admin.controller.AddNewTerminalController;
import client.admin.view.AddNewTerminalView;
import client.admin.view.ModifyTerminalStatusView;
import client.student.model.CreateReservationModel;
import client.student.view.CreateReservationView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.utility.Terminal;

import java.io.IOException;

public class CreateReservationController {

    private final CreateReservationView view;
    private final CreateReservationModel model;
    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();


    public CreateReservationController(CreateReservationView view) {
        this.view = view;
        this.model = new CreateReservationModel();
    }

    public void loadTerminalData() {
        terminalData = model.loadTerminalData();
        view.setTerminalData(terminalData);
    }

    public static void redirectCreateReservationWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(CreateReservationController.class.getResource("/fxml/client/add_reservation_window.fxml"));
            Parent root = loader.load();
            CreateReservationView view = loader.getController();
            CreateReservationController controller = new CreateReservationController(view);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Add Reservation GUI: " + e.getMessage());
        }
    }

}
