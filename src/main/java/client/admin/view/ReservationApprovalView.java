package client.admin.view;

import client.admin.controller.ReservationApprovalController;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import server.utility.ApprovalReservation;

public class ReservationApprovalView {

    @FXML
    private Button searchButton, refreshButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    private TextField searchStudResTextField;
    @FXML
    private TableView<ApprovalReservation> approveResTableView;
    @FXML
    private TableColumn<ApprovalReservation, String> reservationIdColumn, userIdColumn, terminalNumberColumn,
            roomNumberColumn, dateColumn, startTimeColumn, endTimeColumn;
    @FXML
    private TableColumn<ApprovalReservation, String> statusColumn;
    private ReservationApprovalController controller = new ReservationApprovalController(this);

    private final ObservableList<ApprovalReservation> reservationData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("Initializing ReservationApprovalView...");

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        terminalNumberColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        statusColumn.setCellFactory(createStyledStatusCellFactory());
        approveResTableView.setItems(reservationData);

        if (controller != null) {
            controller.loadReservationData();
        }

        setActionSearchButton(event -> controller.searchTerminals(searchStudResTextField.getText()));
        setActionRefreshButton(event -> controller.loadReservationData());
        setActionSaveChangesButton(event -> controller.saveChanges());

    }
    public TableView<ApprovalReservation> getApproveResTableView() {
        return approveResTableView;
    }

    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }

    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
    }

    public void setActionSaveChangesButton(EventHandler<ActionEvent> event) {
        saveChangesButton.setOnAction(event);
    }

    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }

    public void setReservationData(ObservableList<ApprovalReservation> data) {
        reservationData.setAll(data); // Update dataset
        approveResTableView.setItems(null); // Force reset
        approveResTableView.setItems(reservationData); // Reload table data
        approveResTableView.refresh(); // Force UI refresh
        System.out.println("[DEBUG] Reservation data updated. New table size: " + reservationData.size());
    }

    private Callback<TableColumn<ApprovalReservation, String>, TableCell<ApprovalReservation, String>> createStyledStatusCellFactory() {
        return column -> new TableCell<>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>(
                    FXCollections.observableArrayList("Pending", "Approved", "Rejected")
            );
            {
                statusComboBox.setStyle("-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-family: 'System';");
                statusComboBox.setOnAction(e -> {
                    ApprovalReservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        reservation.statusProperty().set(statusComboBox.getValue());
                    }
                });
            }
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ApprovalReservation reservation = getTableRow().getItem();
                    statusComboBox.setValue(reservation.getStatus());

                    int rowIndex = getIndex();
                    Color rowColor = (rowIndex % 2 == 1) ? Color.web("#f8f8f8") : Color.WHITE;
                    setBackground(new Background(new BackgroundFill(rowColor, new CornerRadii(5), null)));

                    statusComboBox.setStyle("-fx-background-color: " +
                            toRGBCode(rowColor) + "; " +
                            "-fx-border-color: transparent; " +
                            "-fx-padding: 5px; " +
                            "-fx-font-size: 13px; " +
                            "-fx-font-family: 'System';");

                    statusComboBox.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(statusComboBox);
                }
            }
            private String toRGBCode(Color color) {
                return String.format("#%02X%02X%02X",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
            }
        };
    }

    public void updateTable(ObservableList<ApprovalReservation> reservations) {
        if (reservations != null && !reservations.isEmpty()) {
            reservationData.setAll(reservations);
            approveResTableView.setItems(reservationData);
        } else {
            System.out.println("No data available to populate the table.");
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
