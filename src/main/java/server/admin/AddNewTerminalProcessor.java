package server.admin;

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

public class AddNewTerminalProcessor {

    private static final String FILE_PATH = "src/main/java/server/util/terminal.xml";

    public static boolean processTerminalData(String terminalId, String room, String osType, String status, String reservationDate, String selectedTimeRange) {
        try {
            // Split the selected time range into start_time and end_time
            String[] timeParts = selectedTimeRange.split("-");
            String startTime = timeParts[0].trim();
            String endTime = timeParts[1].trim();

            // Proceed with the rest of your method
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            System.out.println("[DEBUG] Checking if terminal ID " + terminalId + " exists in room " + room);

            // Proceed to add the terminal if validation is passed
            Element newTerminal = doc.createElement("Terminal");

            Element id = doc.createElement("terminal_id");
            id.appendChild(doc.createTextNode("PC" + terminalId.trim()));
            newTerminal.appendChild(id);

            Element roomElement = doc.createElement("terminal_room");
            roomElement.appendChild(doc.createTextNode(room.trim()));
            newTerminal.appendChild(roomElement);

            Element os = doc.createElement("terminal_os");
            os.appendChild(doc.createTextNode(osType.trim()));
            newTerminal.appendChild(os);

            Element statusElement = doc.createElement("terminal_status");
            statusElement.appendChild(doc.createTextNode(status.trim()));
            newTerminal.appendChild(statusElement);

            // Adding day and time
            Element reservationDateElement = doc.createElement("reservation_date");
            reservationDateElement.appendChild(doc.createTextNode(reservationDate.trim()));
            newTerminal.appendChild(reservationDateElement);

            // Set the split start_time and end_time
            Element startTimeElement = doc.createElement("start_time");
            startTimeElement.appendChild(doc.createTextNode(startTime));
            newTerminal.appendChild(startTimeElement);

            Element endTimeElement = doc.createElement("end_time");
            endTimeElement.appendChild(doc.createTextNode(endTime));
            newTerminal.appendChild(endTimeElement);

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

    public static List<Terminal> parseXML(String filePath) {
        List<Terminal> terminals = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File xmlFile = new File(filePath);
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList terminalNodes = document.getElementsByTagName("Terminal");

            for (int i = 0; i < terminalNodes.getLength(); i++) {
                Node node = terminalNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOS = getTagValue("terminal_os", element);
                    String reservationDate = getTagValue("reservation_date", element);
                    String startTime = getTagValue("start_time", element);
                    String endTime = getTagValue("end_time", element);
                    String terminalStatus = getTagValue("terminal_status", element);

                    Terminal terminal = new Terminal(terminalId, terminalRoom, terminalOS, terminalStatus, reservationDate, startTime, endTime);
                    terminals.add(terminal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return terminals;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }
}
