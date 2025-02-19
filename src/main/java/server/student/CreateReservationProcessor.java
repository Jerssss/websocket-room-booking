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
    private static final String RESERVATION_FILE_PATH = "src/main/java/server/util/reservation_approval.xml";

    public static List<Terminal> parseXML(String filePath) {
        System.out.println("[DEBUG] Parsing XML file at: " + filePath);
        List<Terminal> reservation = new ArrayList<>();

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("[DEBUG] File not found: " + filePath);
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

                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOs = getTagValue("terminal_os", element);
                    String terminalStatus = getTagValue("terminal_status", element);
                    String startTime = getTagValue("start_time", element);
                    String endTime = getTagValue("end_time", element);

                    // Only add terminal if its status is "Active"
                    if (terminalStatus != null && terminalStatus.equalsIgnoreCase("Active")) {
                        System.out.println("[DEBUG] Parsed Active Terminal: ID=" + terminalId + ", Room=" + terminalRoom +
                                ", OS=" + terminalOs + ", Status=" + terminalStatus +
                                ", Start Time=" + startTime + ", End Time=" + endTime);
                        reservation.add(new Terminal(terminalId, terminalRoom, terminalOs, terminalStatus, startTime, endTime));
                    } else {
                        System.out.println("[DEBUG] Skipping Terminal ID=" + terminalId + " because status is not Active.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Error parsing XML: " + e.getMessage());
            e.printStackTrace();
        }
        return reservation;
    }

    public static boolean processReservationData(String reservationId, String userId, String terminalId,
                                                 String roomId, String reservationDate, String startTime,
                                                 String endTime, String status) {
        System.out.println("[DEBUG] Starting processReservationData for reservationId=" + reservationId);

        // 1) Force terminalId to have "PC" prefix before saving to XML
        terminalId = ensurePCPrefix(terminalId);
        System.out.println("[DEBUG] Final terminalId used in XML: " + terminalId);

        try {
            File xmlFile = new File(RESERVATION_FILE_PATH);
            if (!xmlFile.exists()) {
                System.out.println("[DEBUG] Reservation file not found: " + RESERVATION_FILE_PATH);
                return false;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            System.out.println("[DEBUG] Checking for terminal reservation conflict for terminalId " + terminalId);
            if (isTerminalIdExistsInRoomAndDate(root, terminalId, roomId, reservationDate, startTime, endTime)) {
                System.out.println("[DEBUG] Conflict found: Terminal ID " + terminalId + " already reserved for this room and date/time.");
                return false;
            }

            // 2) Create the new <Reservation> element
            Element newReservation = doc.createElement("Reservation");

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

            // 3) Append to the root and write out to XML
            root.appendChild(newReservation);
            System.out.println("[DEBUG] Reservation appended to XML.");

            removeWhiteSpaces(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(RESERVATION_FILE_PATH));
            transformer.transform(source, result);

            System.out.println("[DEBUG] Reservation added successfully to XML.");
            return true;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error processing reservation data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // This method ensures the terminal ID is always in the form "PC###".
    private static String ensurePCPrefix(String rawId) {
        if (rawId == null || rawId.trim().isEmpty()) {
            // Fallback: if it's null or empty, just return "PC1" or something
            return "PC1";
        }
        String trimmed = rawId.trim();
        // If the user typed just digits (e.g., "5"), prepend "PC"
        if (trimmed.matches("\\d+")) {
            return "PC" + trimmed;
        }
        // If it already starts with PC (case-insensitive), keep it
        else if (trimmed.matches("(?i)^pc\\d+$")) {
            return trimmed;
        }
        // Otherwise, just prepend "PC" to whatever they typed
        return "PC" + trimmed;
    }

    // Method to check for overlapping reservations
    public static boolean isReservationOverlapping(String terminalId, String roomId, String reservationDate, String startTime, String endTime) {
        System.out.println("[DEBUG] Checking for overlapping reservations in " + RESERVATION_FILE_PATH);
        try {
            File xmlFile = new File(RESERVATION_FILE_PATH);
            if (!xmlFile.exists()) {
                System.out.println("[DEBUG] Reservation file does not exist. No overlaps.");
                return false;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            return isTerminalIdExistsInRoomAndDate(root, ensurePCPrefix(terminalId), roomId, reservationDate, startTime, endTime);
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception while checking for overlapping reservations: " + e.getMessage());
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

            System.out.println("[DEBUG] Checking existing reservation: TerminalID=" + existingTerminalId +
                    ", Room=" + existingRoomId + ", Date=" + existingReservationDate +
                    ", StartTime=" + existingStartTime + ", EndTime=" + existingEndTime);

            // Check for overlapping times in the same room and same terminal
            if (existingTerminalId.equals(terminalId) && existingRoomId.equals(roomId) &&
                    existingReservationDate.equals(reservationDate) &&
                    !(existingEndTime.compareTo(startTime) <= 0 || existingStartTime.compareTo(endTime) >= 0)) {
                return true;
            }
        }
        return false;
    }

    private static void removeWhiteSpaces(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                String text = node.getNodeValue().trim();
                node.setNodeValue(text);
            }
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String content = element.getTextContent().trim();
                if (content.isEmpty()) {
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
