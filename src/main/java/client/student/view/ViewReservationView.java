package client.student.view;

import client.student.model.Reservation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.utility.Terminal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;

public class ViewReservationView {

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
    private TableColumn<Reservation, String> reservationStatusColumn;

    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind columns to Reservation properties
        reservationIDColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIDProperty());
        userIDColumn.setCellValueFactory(cellData -> cellData.getValue().userIDProperty());
        terminalIDColumn.setCellValueFactory(cellData -> cellData.getValue().terminalIDProperty());
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        reservationStatusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Load data from XML
        loadDataFromXML();

        // Add data to TableView
        modResTableView.setItems(reservationData);
    }

    private void loadDataFromXML() {
        try {
            File xmlFile = new File("out/production/9444-team1_preproject/resources/data/Reservations.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(xmlFile));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Reservation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String reservationID = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                    String userID = element.getElementsByTagName("user_id").item(0).getTextContent();
                    String terminalID = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                    String reservationDate = element.getElementsByTagName("reservation_date").item(0).getTextContent();
                    String startTime = element.getElementsByTagName("start_time").item(0).getTextContent();
                    String endTime = element.getElementsByTagName("end_time").item(0).getTextContent();
                    String status = element.getElementsByTagName("status").item(0).getTextContent();

                    reservationData.add(new Reservation(reservationID, userID, terminalID, reservationDate, startTime, endTime, status));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Callback<TableColumn<Terminal, String>, TableCell<Terminal, String>> createStyledStatusCellFactory() {
        return column -> new TableCell<Terminal, String>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>();

            {
                // Set ComboBox options
                statusComboBox.getItems().addAll("Active", "Reserved", "Under Maintenance");

                // Remove default border and add padding for better blending
                statusComboBox.setStyle("-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-family: 'Arial';");

                // Update the terminal status when selection is changed
                statusComboBox.setOnAction(e -> {
                    Terminal terminal = getTableRow().getItem();
                    if (terminal != null) {
                        terminal.setTerminalStatus(statusComboBox.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Terminal terminal = getTableRow().getItem();
                    statusComboBox.setValue(terminal.getTerminalStatus());

                    // ✅ Even lighter gray for odd rows, pure white for even rows
                    int rowIndex = getIndex();
                    Color rowColor = (rowIndex % 2 == 1) ? Color.web("#EEEEEE") : Color.WHITE;

                    // Apply row background
                    setBackground(new Background(new BackgroundFill(rowColor, new CornerRadii(5), null)));

                    // Set ComboBox background to match row color
                    statusComboBox.setStyle("-fx-background-color: " + toRGBCode(rowColor) + "; " +
                            "-fx-border-color: transparent; " +
                            "-fx-padding: 5px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-family: 'Arial';");

                    statusComboBox.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(statusComboBox);
                }
            }

            // Convert Color to RGB hex code
            private String toRGBCode(Color color) {
                return String.format("#%02X%02X%02X",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
            }
        };
    }

}
