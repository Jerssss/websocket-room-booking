package client.admin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.utility.StudentReservation;
import server.utility.ViewStudentReservationXMLParser;

import java.util.List;

public class ViewStudentReservationsView {

    @FXML
    private Button searchButton;
    @FXML
    private TextField searchStudResTextField;

    @FXML
    private TableView<StudentReservation> studResTableView;
    @FXML
    private TableColumn<StudentReservation, String> resIDColumn;
    @FXML
    private TableColumn<StudentReservation, String> terminalColumn;
    @FXML
    private TableColumn<StudentReservation, String> roomNumberColumn;
    @FXML
    private TableColumn<StudentReservation, String> dateColumn;

    @FXML
    private TableColumn<StudentReservation, String> statusColumn1;
    @FXML

    private ObservableList<StudentReservation> studResData = FXCollections.observableArrayList();

    // Setters for event handlers
    public void setActionSearchButton(EventHandler<ActionEvent> event) {
        searchButton.setOnAction(event);
    }

    // Getters for the TextField
    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }

    @FXML
    public void initialize() {
        // Initialize columns with corresponding Terminal property names
        resIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        terminalColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIdProperty());
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().terminalRoomProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusColumn1.setCellValueFactory(cellData -> cellData.getValue().terminalStatusProperty());

        // Load data from XML file
        loadDataFromXML("src/main/java/server/util/reserved.xml");

        // Bind the ObservableList to the TableView
        studResTableView.setItems(studResData);
    }

    // Method to load data from the XML file
    private void loadDataFromXML(String filePath) {
        List<StudentReservation> studentReservations = ViewStudentReservationXMLParser.parseXML(filePath);
        if (studentReservations != null) {
            studResData.addAll(studentReservations);
        }
    }
}