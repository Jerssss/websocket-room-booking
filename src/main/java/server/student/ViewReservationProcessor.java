package server.student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ViewReservationProcessor {
    private final String xmlFilePath = "src/main/java/server/util/reserved.xml"; // Path to the XML file

    // Fetch all reservations from the XML file
    public Map<String, Map<String, String>> fetchAllReservations() {
        Map<String, Map<String, String>> reservations = new HashMap<>();
        try {
            File file = new File(xmlFilePath);
            if (!file.exists()) {
                System.out.println("ERROR: XML file not found at " + xmlFilePath);
                return reservations; // Return empty map if file does not exist
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                Map<String, String> reservation = new HashMap<>();
                reservation.put("reservation_id", element.getElementsByTagName("reservation_id").item(0).getTextContent());
                reservation.put("Student_ID", element.getElementsByTagName("Student_ID").item(0).getTextContent());
                reservation.put("terminal_id", element.getElementsByTagName("terminal_id").item(0).getTextContent());
                reservation.put("terminal_room", element.getElementsByTagName("terminal_room").item(0).getTextContent());
                reservation.put("terminal_status", element.getElementsByTagName("terminal_status").item(0).getTextContent());
                reservations.put(reservation.get("reservation_id"), reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }
}