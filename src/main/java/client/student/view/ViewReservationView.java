package client.student.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Map;

public class ViewReservationView {

    @FXML
    private VBox centerPane; //refers to the entirety of this pane; for calling when needed
    @FXML
    private Label historyLabel;
    @FXML
    private Label timePickerLabel;
    @FXML
    private Label colonLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField dayTextField;
    @FXML
    private TextField monthTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private Button refreshButton;//might rename to avoid confusion
    @FXML
    private TableView<Map<String, String>> modResTableView;
    @FXML
    private TableColumn<?, ?> reservationIDColumn;
    @FXML
    private TableColumn<?, ?> terminalNoColumn;
    @FXML
    private TableColumn<?, ?> roomNoColumn;
    @FXML
    private TableColumn<?, ?> reservationStatusColumn;

    // Event Handler Setup
    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
    }

    // Getters for all components
    public VBox getCenterPane() {
        return centerPane;
    }

    public Label getHistoryLabel() {
        return historyLabel;
    }

    public Label getTimePickerLabel() {
        return timePickerLabel;
    }

    public Label getColonLabel() {
        return colonLabel;
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public TextField getEndTimeTextField() {
        return endTimeTextField;
    }

    public TextField getStartTimeTextField() {
        return startTimeTextField;
    }

    public TextField getDayTextField() {
        return dayTextField;
    }

    public TextField getMonthTextField() {
        return monthTextField;
    }

    public TextField getYearTextField() {
        return yearTextField;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public TableView<Map<String, String>> getModResTableView() {
        return modResTableView;
    }

    public TableColumn<?, ?> getReservationIDColumn() {
        return reservationIDColumn;
    }

    public TableColumn<?, ?> getTerminalNoColumn() {
        return terminalNoColumn;
    }

    public TableColumn<?, ?> getRoomNoColumn() {
        return roomNoColumn;
    }

    public TableColumn<?, ?> getReservationStatusColumn() {
        return reservationStatusColumn;
    }

    // Setters for text fields
    public void setStartTime(String time) {
        startTimeTextField.setText(time);
    }

    public void setEndTime(String time) {
        endTimeTextField.setText(time);
    }

    public void setDate(String month, String day, String year) {
        monthTextField.setText(month);
        dayTextField.setText(day);
        yearTextField.setText(year);
    }
}