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
import java.util.Optional;

public class ModifyReservationView {
    private String sessionToken;
    private ModifyReservationController controller;
    @FXML
    private VBox centerPane;
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

    @FXML
    private Label reservedDateLabel;
    @FXML
    private Label reservedTimeLabel;
    @FXML
    private Label reservationRoomNoLabel;
    @FXML
    private Label reservationTerminalNoLabel;
    @FXML
    private Button sendRequestButton;
    @FXML
    private Button deleteReservationButton;

    private Stage confirmationStage;
    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        if (controller == null) {
            controller = new ModifyReservationController(this, sessionToken);
        }
        controller.loadReservationData(); // Load data after token is set
    }

    private void initializeController() {
        if (sessionToken != null) {
            controller = new ModifyReservationController(this, sessionToken);
            controller.loadReservationData(); // Load data after controller is ready
        }
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
        modResTableView.setItems(reservationData); // Remove the null reset
        modResTableView.refresh();
        System.out.println("Reservation data updated. New table size: " + reservationData.size());
    }

    private Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>> createCancelButtonCellFactory() {
        return column -> new TableCell<>() {
            private final Button cancelButton = new Button("Cancel");

            // Initialize the button once per cell
            {
                cancelButton.setStyle("-fx-background-color: #0d3073; -fx-text-fill: white;");
                cancelButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    if (reservation != null) {
                        // Show confirmation dialog
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
                                // Proceed with cancellation
                                showConfirmationPane(reservation);
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
            dialogController.setReservationDetails(reservation); // Pass the reservation object

            Stage dialogStage = new Stage();
            dialogController.setDialogStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(confirmationPane));

            dialogStage.showAndWait();

            if (dialogController.isDeleteConfirmed()) {
                controller.removeReservation(reservation);
                JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");
            } else {
                // Update the reservation in the main controller
                controller.updateReservation(reservation); // Call the update method
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

    public void deleteButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), deleteReservationButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void sendRequestButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), sendRequestButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void sendRequestButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), sendRequestButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}