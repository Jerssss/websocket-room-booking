package server.admin;

import client.admin.model.AddNewTerminalModel;
import javafx.scene.control.Alert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

public class AddNewTerminalProcessor {

    private static final String FILE_PATH = "src/main/java/server/util/terminal.xml";

    public boolean processTerminalData(AddNewTerminalModel terminalModel) {
        try {
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            // Validate if terminal ID exists in the specified room
            if (isTerminalIdExistsInRoom(root, terminalModel.getTerminalId(), terminalModel.getRoom())) {
                // Show alert if terminal ID already exists
                showAlert("Validation Error", "The Terminal ID already exists in this room.", Alert.AlertType.ERROR);
                return false; // Return false to indicate the validation failed
            }

            // Proceed to add the terminal if validation is passed
            Element newTerminal = doc.createElement("Terminal");

            Element id = doc.createElement("terminal_id");
            id.appendChild(doc.createTextNode("PC" + terminalModel.getTerminalId().trim()));
            newTerminal.appendChild(id);

            Element room = doc.createElement("terminal_room");
            room.appendChild(doc.createTextNode("D" + terminalModel.getRoom().trim()));
            newTerminal.appendChild(room);

            Element os = doc.createElement("terminal_os");
            os.appendChild(doc.createTextNode(terminalModel.getOsType().trim()));
            newTerminal.appendChild(os);

            // Check if room needs a default schedule
            if (terminalModel.getRoom().equals("526") || terminalModel.getRoom().equals("524") || terminalModel.getRoom().equals("426")) {
                Element schedule = doc.createElement("default_schedule");

                String[][] scheduleData = {
                        {"Monday", "09:30-17:30"},
                        {"Tuesday", "09:30-17:30"},
                        {"Wednesday", "11:30-16:30"},
                        {"Thursday", "11:30-16:30"},
                        {"Friday", "07:30-15:30"},
                        {"Saturday", "07:30-15:30"}
                };

                for (String[] day : scheduleData) {
                    Element dayElement = doc.createElement(day[0]);
                    dayElement.appendChild(doc.createTextNode(day[1]));
                    schedule.appendChild(dayElement);
                }
                newTerminal.appendChild(schedule);
            }

            Element status = doc.createElement("terminal_status");
            status.appendChild(doc.createTextNode(terminalModel.getStatus().trim()));
            newTerminal.appendChild(status);

            root.appendChild(newTerminal);

            removeWhiteSpaces(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(FILE_PATH));
            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate if terminal ID already exists for a given room
    private boolean isTerminalIdExistsInRoom(Element root, String terminalId, String room) {
        NodeList terminalNodes = root.getElementsByTagName("Terminal");
        for (int i = 0; i < terminalNodes.getLength(); i++) {
            Element terminalElement = (Element) terminalNodes.item(i);

            String existingTerminalId = terminalElement.getElementsByTagName("terminal_id").item(0).getTextContent();
            String existingRoom = terminalElement.getElementsByTagName("terminal_room").item(0).getTextContent();

            // If the terminal ID matches and the room matches, return true (duplicate found)
            if (existingTerminalId.equals("PC" + terminalId.trim()) && existingRoom.equals("D" + room.trim())) {
                return true;
            }
        }
        return false; // Return false if no duplicates found
    }

    private void removeWhiteSpaces(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhiteSpaces(child);
            }
        }
    }

    // Display an alert message to the user
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}