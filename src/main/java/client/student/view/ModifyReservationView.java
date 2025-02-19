package client.student.view;

import client.student.controller.ModifyReservationController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import server.utility.Reservation;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ModifyReservationView {
    private String sessionToken;
    private ModifyReservationController controller;
    @FXML
    private Button searchButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchStudResTextField;

    @FXML
    private TableView<Reservation> modResTableView;
    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;
    @FXML
    private TableColumn<Reservation, String> roomNumberColumn;
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
    @FXML
    private TableColumn<Reservation, String> editColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        if (controller == null) {
            controller = new ModifyReservationController(this, sessionToken);
        }
        controller.loadReservationData(); // Load data after token is set
    }

    @FXML
    public void initialize() {
        // Initialize UI components only (no controller logic here)
        reservationIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());
        terminalIDColumn.setCellValueFactory(cellData -> cellData.getValue().terminalNumberProperty());
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        statusColumn.setCellValueFactory(param -> new SimpleStringProperty(""));
        statusColumn.setCellFactory(createCancelButtonCellFactory());

        editColumn.setCellValueFactory(param -> new SimpleStringProperty(""));
        editColumn.setCellFactory(createEditButtonCellFactory());

        // Attach button handlers (delegate to controller)
        searchButton.setOnAction(event -> {
            if (controller != null) {
                System.out.println("Search button clicked. Query: " + searchStudResTextField.getText());
                controller.searchReservations(searchStudResTextField.getText());
            }
        });
        refreshButton.setOnAction(event -> {
            if (controller != null) controller.loadReservationData();
        });
        saveChangesButton.setOnAction(event -> {
            if (controller != null) controller.saveChanges();
        });
    }

    public void setReservationData(ObservableList<Reservation> data) {
        reservationData.setAll(data);
        modResTableView.setItems(reservationData);
        modResTableView.refresh();
        System.out.println("Reservation data updated. New table size: " + reservationData.size());
    }

    private Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>> createCancelButtonCellFactory() {
        return column -> new TableCell<>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setStyle("-fx-background-color: #0d3073; -fx-text-fill: white;");
                cancelButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    if (reservation != null) {
                        // Check 24-hour rule
                        LocalDate resDate = LocalDate.parse(reservation.getDate());
                        LocalTime resTime = LocalTime.parse(reservation.getStartTime());
                        LocalDateTime resDateTime = LocalDateTime.of(resDate, resTime);
                        LocalDateTime now = LocalDateTime.now();

                        if (now.isAfter(resDateTime.minusHours(24))) {
                            JOptionPane.showMessageDialog(null, "Cannot cancel reservation within 24 hours of start time.", "Cancellation Error", JOptionPane.ERROR_MESSAGE);
                            return; // Exit without showing the confirmation dialog
                        }

                        // Proceed with confirmation dialog
                        Alert alert = new Alert(
                                Alert.AlertType.CONFIRMATION,
                                "Are you sure you want to cancel this reservation?",
                                ButtonType.YES,
                                ButtonType.NO
                        );
                        alert.setTitle("Cancel Reservation");
                        alert.setHeaderText(null);

                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.YES) {
                                controller.removeReservation(reservation);
                                JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");
                            }
                        });
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        };
    }

    private Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>> createEditButtonCellFactory() {
        return column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setStyle("-fx-background-color: #0d3073; -fx-text-fill: white;");
                editButton.setOnAction(event -> {
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        showConfirmationPane(reservation);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };
    }

    private void showConfirmationPane(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/modify_reservation_window.fxml"));
            BorderPane confirmationPane = loader.load();

            ModifyReservationDialogController dialogController = loader.getController();
            dialogController.setReservationDetails(reservation);

            // Get reservations through the controller
            dialogController.setExistingReservations(controller.getAllReservations());

            Stage dialogStage = new Stage();
            dialogController.setDialogStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(confirmationPane));
            dialogStage.showAndWait();

            if (dialogController.isDeleteConfirmed()) {
                controller.removeReservation(reservation);
                JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");
            } else if (dialogController.isChangesMade()) {
                controller.updateReservation(reservation);
                JOptionPane.showMessageDialog(null, "Reservation status updated to Pending!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveChangesButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), saveChangesButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void saveChangesButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), saveChangesButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void searchButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), searchButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void searchButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), searchButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}