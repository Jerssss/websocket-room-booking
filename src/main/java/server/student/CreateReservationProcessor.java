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
    private static final String RESERVATION_FILE_PATH = "src/main/java/server/util/reservation_approval.xml"; // Reservation file path

    public static List<Terminal> parseXML(String filePath) {
        List<Terminal> reservation = new ArrayList<>();

        try {
            // Debugging statement to check if file exists
            System.out.println("Attempting to parse XML file at: " + filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return reservation;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            document.getDocumentElement().normalize();
            NodeList terminalNodes = document.getElementsByTagName("Terminal");

            for (int i = 0; i < terminalNodes.getLength(); i++) {
                Node node = terminalNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Parsing the updated XML structure
                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOs = getTagValue("terminal_os", element);
                    String terminalStatus = getTagValue("terminal_status", element); // New field
                    String reservationDate = getTagValue("reservation_date", element); // Updated field
                    String startTime = getTagValue("start_time", element); // Updated field
                    String endTime = getTagValue("end_time", element); // Updated field

                    // Debugging statement to ensure correct parsing
                    System.out.println("Parsed Terminal: ID=" + terminalId + ", Room=" + terminalRoom +
                            ", OS=" + terminalOs + ", Status=" + terminalStatus + ", Date=" + reservationDate +
                            ", Start Time=" + startTime + ", End Time=" + endTime);

                    // Assuming the Terminal constructor matches the updated structure
                    reservation.add(new Terminal(terminalId, terminalRoom, terminalOs, terminalStatus, reservationDate, startTime, endTime));
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing XML: " + e.getMessage());
            e.printStackTrace();
        }
        return reservation;
    }


    public static boolean processReservationData(String reservationId, String userId, String terminalId,
                                                 String roomId, String reservationDate, String startTime,
                                                 String endTime, String status) {
        try {
            File xmlFile = new File(RESERVATION_FILE_PATH);
            if (!xmlFile.exists()) {
                System.out.println("Reservation file not found: " + RESERVATION_FILE_PATH);
                return false;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            // Debugging statement to check if terminal reservation exists for the same room/date
            System.out.println("Checking if terminal ID " + terminalId + " is reserved for the same date/time...");
            if (isTerminalIdExistsInRoomAndDate(root, terminalId, roomId, reservationDate, startTime, endTime)) {
                System.out.println("Error: Terminal ID already reserved in this room for the specified date and time.");
                return false;
            }

            // Proceed to add the reservation if validation is passed
            Element newReservation = doc.createElement("Reservation");

            // Create and append reservation elements
            Element id = doc.createElement("reservation_id");
            id.appendChild(doc.createTextNode(reservationId));
            newReservation.appendChild(id);

            Element userIdElement = doc.createElement("user_id");
            userIdElement.appendChild(doc.createTextNode(userId));
            newReservation.appendChild(userIdElement);

            Element terminalIdElement = doc.createElement("terminal_id");
            terminalIdElement.appendChild(doc.createTextNode(terminalId));
            newReservation.appendChild(terminalIdElement);

            Element roomIdElement = doc.createElement("room_id");
            roomIdElement.appendChild(doc.createTextNode(roomId));
            newReservation.appendChild(roomIdElement);

            Element reservationDateElement = doc.createElement("reservation_date");
            reservationDateElement.appendChild(doc.createTextNode(reservationDate));
            newReservation.appendChild(reservationDateElement);

            Element startTimeElement = doc.createElement("start_time");
            startTimeElement.appendChild(doc.createTextNode(startTime));
            newReservation.appendChild(startTimeElement);

            Element endTimeElement = doc.createElement("end_time");
            endTimeElement.appendChild(doc.createTextNode(endTime));
            newReservation.appendChild(endTimeElement);

            Element statusElement = doc.createElement("status");
            statusElement.appendChild(doc.createTextNode(status));
            newReservation.appendChild(statusElement);

            // Add the new reservation to the root
            root.appendChild(newReservation);

            // Debugging statement for XML update process
            System.out.println("Adding reservation to XML...");

            // Remove white spaces from the document
            removeWhiteSpaces(doc);

            // Save the updated XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(RESERVATION_FILE_PATH));
            transformer.transform(source, result);

            System.out.println("Reservation added successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("Error processing reservation data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isTerminalIdExistsInRoomAndDate(Element root, String terminalId, String roomId,
                                                           String reservationDate, String startTime, String endTime) {
        NodeList reservations = root.getElementsByTagName("Reservation");
        for (int i = 0; i < reservations.getLength(); i++) {
            Element reservation = (Element) reservations.item(i);
            String existingTerminalId = reservation.getElementsByTagName("terminal_id").item(0).getTextContent();
            String existingRoomId = reservation.getElementsByTagName("room_id").item(0).getTextContent();
            String existingReservationDate = reservation.getElementsByTagName("reservation_date").item(0).getTextContent();
            String existingStartTime = reservation.getElementsByTagName("start_time").item(0).getTextContent();
            String existingEndTime = reservation.getElementsByTagName("end_time").item(0).getTextContent();

            // Debugging: Checking the conflicting reservation data
            System.out.println("Existing reservation: TerminalID=" + existingTerminalId + ", Room=" + existingRoomId +
                    ", Date=" + existingReservationDate + ", StartTime=" + existingStartTime +
                    ", EndTime=" + existingEndTime);

            if (existingTerminalId.equals(terminalId) && existingRoomId.equals(roomId) &&
                    existingReservationDate.equals(reservationDate) &&
                    !(existingEndTime.compareTo(startTime) <= 0 || existingStartTime.compareTo(endTime) >= 0)) {
                return true; // Conflict found
            }
        }
        return false; // No conflict found
    }

    private static void removeWhiteSpaces(Document doc) {
        // First, clean up all text nodes (trim them)
        NodeList nodeList = doc.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                String text = node.getNodeValue().trim();
                node.setNodeValue(text);
            }
        }

        // Second, remove unwanted whitespace between elements (unnecessary spaces between tags)
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String content = element.getTextContent().trim();
                if (content.isEmpty()) {
                    // If the element content is empty after trimming, remove it
                    element.setTextContent("");
                }
            }
        }
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
