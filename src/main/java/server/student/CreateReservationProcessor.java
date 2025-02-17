package server.student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.utility.Terminal;

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

public class CreateReservationProcessor {
    private static final String FILE_PATH = "src/main/java/server/util/terminal.xml";

    public static List<Terminal> parseXML(String filePath) {
        List<Terminal> reservation = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            document.getDocumentElement().normalize();
            NodeList terminalNodes = document.getElementsByTagName("Terminal");

            for (int i = 0; i < terminalNodes.getLength(); i++) {
                Node node = terminalNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOs = getTagValue("terminal_os", element);
                    String terminalDate = getTagValue("day", element);
                    String terminalTime = getTagValue("time", element);

                    reservation.add(new Terminal(terminalId, terminalRoom, terminalOs, terminalDate, terminalTime));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public static boolean processReservationData(String terminalId, String room, String osType, String selectedDay, String selectedTime) {
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
                System.out.println("Error: Terminal ID already reserved in this room.");
                return false;
            }

            // Proceed to add the reservation if validation is passed
            Element newReservation = doc.createElement("Reservation");

            Element id = doc.createElement("terminal_id");
            id.appendChild(doc.createTextNode("PC" + terminalId.trim()));
            newReservation.appendChild(id);

            Element roomElement = doc.createElement("terminal_room");
            roomElement.appendChild(doc.createTextNode(room.trim()));
            newReservation.appendChild(roomElement);

            Element os = doc.createElement("terminal_os");
            os.appendChild(doc.createTextNode(osType.trim()));
            newReservation.appendChild(os);

            Element dayElement = doc.createElement("day");
            dayElement.appendChild(doc.createTextNode(selectedDay.trim()));
            newReservation.appendChild(dayElement);

            Element timeElement = doc.createElement("time");
            timeElement.appendChild(doc.createTextNode(selectedTime.trim()));
            newReservation.appendChild(timeElement);

            root.appendChild(newReservation);

            removeWhiteSpaces(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(FILE_PATH));
            transformer.transform(source, result);

            System.out.println("Reservation added successfully.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate if terminal ID already reserved for a given room
    private static boolean isTerminalIdExistsInRoom(Element root, String terminalId, String room) {
        NodeList terminalNodes = root.getElementsByTagName("Reservation");
        for (int i = 0; i < terminalNodes.getLength(); i++) {
            Element terminalElement = (Element) terminalNodes.item(i);

            String existingTerminalId = terminalElement.getElementsByTagName("terminal_id").item(0).getTextContent();
            String existingRoom = terminalElement.getElementsByTagName("terminal_room").item(0).getTextContent();

            // If the terminal ID matches and the room matches, return true (duplicate found)
            if (existingTerminalId.equals("PC" + terminalId.trim()) && existingRoom.equals(room.trim())) {
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
