package server.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.utility.Terminal;
import server.utility.TerminalVer2;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddNewTerminalProcessor {

    private static final String FILE_PATH = "src/main/java/server/util/terminal.xml";

    public static boolean processTerminalData(String terminalId, String room, String osType, String status, String selectedDay, String selectedTime) {
        try {
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            // Validate if terminal ID exists in the specified room
            if (isTerminalIdExistsInRoom(root, terminalId, room)) {
                System.out.println("Error: Terminal ID already exists in this room.");
                return false;
            }

            // Proceed to add the terminal if validation is passed
            Element newTerminal = doc.createElement("Terminal");

            Element id = doc.createElement("terminal_id");
            id.appendChild(doc.createTextNode("PC" + terminalId.trim()));
            newTerminal.appendChild(id);

            Element roomElement = doc.createElement("terminal_room");
            roomElement.appendChild(doc.createTextNode("D" + room.trim()));
            newTerminal.appendChild(roomElement);

            Element os = doc.createElement("terminal_os");
            os.appendChild(doc.createTextNode(osType.trim()));
            newTerminal.appendChild(os);

            Element statusElement = doc.createElement("terminal_status");
            statusElement.appendChild(doc.createTextNode(status.trim()));
            newTerminal.appendChild(statusElement);

            // Adding day and time (Fixes previous issues)
            Element dayElement = doc.createElement("day");
            dayElement.appendChild(doc.createTextNode(selectedDay.trim())); // Fix
            newTerminal.appendChild(dayElement);

            Element timeElement = doc.createElement("time");
            timeElement.appendChild(doc.createTextNode(selectedTime.trim())); // Fix
            newTerminal.appendChild(timeElement);

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

            System.out.println("Terminal added successfully.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate if terminal ID already exists for a given room
    private static boolean isTerminalIdExistsInRoom(Element root, String terminalId, String room) {
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

    private static void removeWhiteSpaces(Node node) {
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
    public static List<TerminalVer2> parseXML(String filePath) {
        List<TerminalVer2> terminals = new ArrayList<>();

        try {
            // Initialize DocumentBuilderFactory and DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file
            File xmlFile = new File(filePath);
            Document document = builder.parse(xmlFile);

            // Normalize the XML structure
            document.getDocumentElement().normalize();

            // Get all <Terminal> nodes
            NodeList studentNodes = document.getElementsByTagName("Terminal");

            // Loop through the nodes and extract data
            for (int i = 0; i < studentNodes.getLength(); i++) {
                Node node = studentNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get the data for each terminal
                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOS = getTagValue("terminal_os", element);
                    String day = getTagValue("day", element);
                    String time = getTagValue("time", element);
                    String terminalStatus = getTagValue("terminal_status", element);

                    // Create a new Terminal object and add it to the list
                    TerminalVer2 terminal = new TerminalVer2(terminalId, terminalRoom, terminalOS, terminalStatus, day, time);
                    terminals.add(terminal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return terminals;
    }

    // Helper method to extract the value of a tag
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }
}
