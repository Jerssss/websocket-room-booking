package client.student.controller;

import client.student.view.ModifyReservationView;
import client.student.model.ModifyReservationModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import server.utility.Reservation;

import java.io.IOException;

public class ModifyReservationController {
    private ModifyReservationView view;
    private ModifyReservationModel model;
    private String studentID;
    private static BorderPane rootPane; // Reference to the root pane for loading views

    public ModifyReservationController(ModifyReservationView view, String studentID, BorderPane rootPane) {
        this.view = view;
        this.studentID = studentID;
        this.rootPane = rootPane; // Initialize the root pane

        try {
            this.model = new ModifyReservationModel(studentID);
            initializeController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeController() {
        setupEventHandlers();
        configureTableViewColumns();
    }

    private void setupEventHandlers() {
        view.setActionSearchButton(this::handleSearch);
        view.setActionSaveChangesButton(this::handleSaveChanges);
    }

    private void configureTableViewColumns() {
    }

    private void handleSearch(ActionEvent event) {
    }

    private void handleSaveChanges(ActionEvent event) {
    }

    private void openModifyReservationWindow(Reservation reservation) {
        // Open the ModifyReservationWindowView for the selected reservation
        ModifyReservationController.loadView("/fxml/client/Modify_reservation_window.fxml");
    }

    public static void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ModifyReservationController.class.getResource(fxmlFile));
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view); // Set the loaded view as the center of the root pane
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}