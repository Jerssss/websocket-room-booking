package client.student.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TerminalPickerWindowView implements Initializable {

    @FXML
    private Label roomLabel;
    @FXML
    private ScrollPane terminalsScrollPane;
    @FXML
    private AnchorPane terminalsAnchorPane;
    @FXML
    private Button returnButton;
    @FXML
    private GridPane terminalGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        returnButton.setOnAction(event -> handleRefreshButton());
    }

    public void setRoomName(String roomName) {
        roomLabel.setText(roomName); // Set room name
        loadTerminals(roomName); // Load terminals dynamically
    }

    private void handleRefreshButton() {
        terminalGridPane.getChildren().clear();
        loadTerminals(roomLabel.getText()); // Reload terminals
    }

    private void loadTerminals(String selectedRoom) {
        terminalGridPane.getChildren().clear();

        try {
            File xmlFile = new File("out/production/9444-team1_preproject/resources/data/terminals.xml"); // Update with your actual XML path
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Terminal");
            System.out.println("Total Terminals Found: " + nodeList.getLength()); // Debug log

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element terminalElement = (Element) node;
                    String roomCode = terminalElement.getElementsByTagName("terminal_room").item(0).getTextContent();

                    if (roomCode.equals(selectedRoom)) { // Filter by room
                        String terminalID = terminalElement.getElementsByTagName("terminal_id").item(0).getTextContent();
                        String os = terminalElement.getElementsByTagName("terminal_os").item(0).getTextContent();
                        String status = terminalElement.getElementsByTagName("terminal_status").item(0).getTextContent();
                        String availableTime = getAvailabilityForToday(terminalElement);

                        // Debug log
                        System.out.println("Terminal ID: " + terminalID);
                        System.out.println("Room Code: " + roomCode);
                        System.out.println("OS: " + os);
                        System.out.println("Status: " + status);
                        System.out.println("Available Time: " + availableTime);

                        addTerminalCard(terminalID, availableTime, os);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getAvailabilityForToday(Element terminalElement) {
        String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
        dayOfWeek = dayOfWeek.substring(0, 1) + dayOfWeek.substring(1).toLowerCase(); // Convert to XML format (e.g., "Monday")

        // Get the <default_schedule> node
        NodeList scheduleList = terminalElement.getElementsByTagName("default_schedule");
        if (scheduleList.getLength() == 0) {
            return dayOfWeek + ": No Schedule Found"; // Handle missing <default_schedule>
        }

        Node scheduleNode = scheduleList.item(0);
        if (scheduleNode == null) {
            return dayOfWeek + ": No Schedule Found"; // Handle null schedule node
        }

        NodeList days = scheduleNode.getChildNodes();
        for (int i = 0; i < days.getLength(); i++) {
            Node node = days.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element dayElement = (Element) node;
                if (dayElement.getTagName().equals(dayOfWeek)) {
                    return dayOfWeek + ": " + dayElement.getTextContent(); // Return the current day's availability
                }
            }
        }
        return dayOfWeek + ": Closed"; // Default fallback if no schedule is found
    }
    public void addTerminalCard(String pcName, String timeAvailable, String os) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_item_card.fxml"));
            HBox terminalCard = loader.load();

            TerminalItemCardView controller = loader.getController();
            controller.setPcName(pcName);
            controller.setTimeAvailable(timeAvailable);
            controller.setOS(os);
            controller.setCurrentDate(LocalDate.now().toString()); // Pass current date to the terminal card

            int totalCards = terminalGridPane.getChildren().size();
            int columnIndex = totalCards % 2;
            int rowIndex = totalCards / 2;

            terminalGridPane.add(terminalCard, columnIndex, rowIndex);
            GridPane.setMargin(terminalCard, new Insets(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
