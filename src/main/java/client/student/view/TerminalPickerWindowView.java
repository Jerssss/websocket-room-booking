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

                        addTerminalCard(terminalID, availableTime, os, status);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAvailabilityForToday(Element terminalElement) {
        String dayOfWeek = java.time.LocalDate.now().getDayOfWeek().name(); // Get current day
        NodeList schedule = terminalElement.getElementsByTagName("default_schedule").item(0).getChildNodes();

        for (int i = 0; i < schedule.getLength(); i++) {
            Node node = schedule.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element dayElement = (Element) node;
                if (dayElement.getTagName().equalsIgnoreCase(dayOfWeek)) {
                    return dayElement.getTextContent();
                }
            }
        }
        return "Closed";
    }

    public void addTerminalCard(String pcName, String timeAvailable, String os, String status) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/terminal_item_card.fxml"));
            HBox terminalCard = loader.load();

            TerminalItemCardView controller = loader.getController();
            controller.setPcName(pcName);
            controller.setTimeAvailable(timeAvailable);
            controller.setOS(os);
            controller.setAvailability(status);

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
