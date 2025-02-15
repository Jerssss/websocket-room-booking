package client.student.model;

import client.utility.ServerConnection;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.HashMap;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;

public class ModifyReservationModel {
    private final ServerConnection serverConnection;
    private final String studentID;  // Store student ID for filtering

    public ModifyReservationModel(String studentID) throws IOException {
        this.serverConnection = new ServerConnection();
        this.studentID = studentID;
    }

    public Map<String, Map<String, String>> fetchReservation() {
        try {
            // Send fetch reservation request
            serverConnection.sendMessage("FETCH_RESERVATION");
            String response = serverConnection.readMessage();

            // Parse all reservations and filter by student ID
            Map<String, Map<String, String>> allReservations = parseReservation(response);
            return filterByStudentID(allReservations);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateReservation(String reservationId, Map<String, String> updates) {
        try {
            String xmlUpdate = createUpdateXml(reservationId, updates);
            serverConnection.sendMessage("UPDATE_RESERVATION" + xmlUpdate);
            String response = serverConnection.readMessage();
            return response.contains("<Status>SUCCESS</Status>");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createUpdateXml(String reservationId, Map<String, String> updates) {
        StringBuilder xml = new StringBuilder();
        xml.append("<UpdateRequest>")
                .append("<ReservationID>").append(reservationId).append("</ReservationID>");
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            xml.append("<").append(entry.getKey()).append(">")
                    .append(entry.getValue())
                    .append("</").append(entry.getKey()).append(">");
        }
        xml.append("</UpdateRequest>");
        return xml.toString();
    }

    private Map<String, Map<String, String>> filterByStudentID(Map<String, Map<String, String>> reservations) {
        Map<String, Map<String, String>> filtered = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : reservations.entrySet()) {
            Map<String, String> reservation = entry.getValue();
            if (reservation.get("Student_ID").equals(studentID)) {
                filtered.put(entry.getKey(), reservation);
            }
        }
        return filtered;
    }

    private Map<String, Map<String, String>> parseReservation(String response) {
        Map<String, Map<String, String>> reservations = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            NodeList reservationNodes = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < reservationNodes.getLength(); i++) {
                Element reservationElement = (Element) reservationNodes.item(i);
                Map<String, String> reservationData = new HashMap<>();

                String reservationId = getTagValue("reservation_id", reservationElement);
                reservationData.put("reservation_id", reservationId);
                reservationData.put("Student_ID", getTagValue("Student_ID", reservationElement));
                reservationData.put("terminal_id", getTagValue("terminal_id", reservationElement));
                reservationData.put("terminal_room", getTagValue("terminal_room", reservationElement));
                reservationData.put("terminal_status", getTagValue("terminal_status", reservationElement));

                reservations.put(reservationId, reservationData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    public void closeConnection() {
        serverConnection.close();
    }
}