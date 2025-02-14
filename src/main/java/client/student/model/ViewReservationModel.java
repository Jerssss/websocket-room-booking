package client.student.model;

import client.utility.ServerConnection;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.HashMap;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;

public class ViewReservationModel {
    private final ServerConnection serverConnection;

    public ViewReservationModel() throws IOException {
        this.serverConnection = new ServerConnection();
    }

    // Fetch reservations from the server and return as a Map
    public Map<String, Map<String, String>> fetchAllReservations() {
        try {
            // Send request for reservations
            serverConnection.sendMessage("FETCH_RESERVATIONS");
            String response = serverConnection.readMessage();

            // Log raw server response
            System.out.println("Server Response: " + response);

            // Parse and return the data
            return parseReservations(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Parse the server's response (XML format) into a Map
    private Map<String, Map<String, String>> parseReservations(String response) {
        Map<String, Map<String, String>> reservations = new HashMap<>();
        try {
            // Parse the XML response
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(response));
            Document doc = builder.parse(is);

            // Extract the reservations
            NodeList nodeList = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element reservationElement = (Element) nodeList.item(i);

                String reservationId = reservationElement.getElementsByTagName("reservation_id").item(0).getTextContent();
                String studentId = reservationElement.getElementsByTagName("Student_ID").item(0).getTextContent();
                String terminalId = reservationElement.getElementsByTagName("terminal_id").item(0).getTextContent();
                String terminalRoom = reservationElement.getElementsByTagName("terminal_room").item(0).getTextContent();
                String terminalStatus = reservationElement.getElementsByTagName("terminal_status").item(0).getTextContent();

                // Store the reservation data in a map
                Map<String, String> reservationData = new HashMap<>();
                reservationData.put("reservation_id", reservationId);
                reservationData.put("Student_ID", studentId);
                reservationData.put("terminal_id", terminalId);
                reservationData.put("terminal_room", terminalRoom);
                reservationData.put("terminal_status", terminalStatus);

                // Put the reservation data in the main map using reservation_id as the key
                reservations.put(reservationId, reservationData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }


    // Helper method to extract text value of a tag
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    public void closeConnection() {
        serverConnection.close();
    }
}
