// File: client/admin/model/ReservationApprovalModel.java
package client.admin.model;
//hello

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import server.utility.ApprovalReservation;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReservationApprovalModel {

    private ServerConnection serverConnection;

    public ReservationApprovalModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server connection failed.");
        }
    }

    public List<ApprovalReservation> fetchAllApprovalReservations() {
        List<ApprovalReservation> reservations = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return reservations;
        }

        try {
            serverConnection.sendMessage("<Request><Type>ViewReservationApprovals</Type></Request>");
            Thread.sleep(1000); // Give time for response
            StringBuilder responseBuilder = new StringBuilder();
            String chunk;
            while ((chunk = serverConnection.readMessage()) != null) {
                responseBuilder.append(chunk);
                if (chunk.contains("</Reservations>")) { // Ensure full XML is received
                    break;
                }
            }
            String responseXML = responseBuilder.toString();

            //DEBUGGERS
//            System.out.println("Received XML Length: " + (responseXML != null ? responseXML.length() : "NULL"));
//            System.out.println("Received XML Content: [" + responseXML + "]");

            reservations = parseXMLResponse(responseXML);
//            System.out.println("Parsed reservations: " + reservations.size()); // Debug print
        } catch (IOException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return reservations;
    }

    // Method to update reservation status and save it to XML
    public void updateReservationStatus(String reservationId, String newStatus) {
        // Logic to find and update the status of the reservation in the list
        // This could involve updating the status in the internal list of reservations.
    }

    // Method to save updated reservations back to the XML file
    public void saveUpdatedReservationsToXML(List<ApprovalReservation> reservations) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("src/main/java/server/util/reservation_approval.xml"));

            NodeList reservationNodes = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < reservationNodes.getLength(); i++) {
                Element reservationElement = (Element) reservationNodes.item(i);
                String reservationId = reservationElement.getElementsByTagName("reservation_id").item(0).getTextContent();

                // Find the matching reservation and update its status
                for (ApprovalReservation reservation : reservations) {
                    if (reservation.getReservationId().equals(reservationId)) {
                        reservationElement.getElementsByTagName("status").item(0).setTextContent(reservation.getStatus());
                    }
                }
            }

            // Save the updated XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/java/server/util/reservation_approval.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ApprovalReservation> parseXMLResponse(String xmlResponse) {
        List<ApprovalReservation> reservations = new ArrayList<>();
        try {
//            System.out.println("Parsing XML Response: " + xmlResponse); // Debug print

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);  // Ignore whitespace
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

            doc.getDocumentElement().normalize(); // Normalize XML structure
            NodeList nodeList = doc.getElementsByTagName("Reservation");

//            System.out.println("Found " + nodeList.getLength() + " reservations."); // Debug print

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                // Debugging each field extraction
                System.out.println("Processing reservation " + (i + 1));

                String reservationId = getTagValue("reservation_id", element);
                String userId = getTagValue("user_id", element);
                String terminalId = getTagValue("terminal_id", element);
                String roomId = getTagValue("room_id", element);
                String reservationDate = getTagValue("reservation_date", element);
                String startTime = getTagValue("start_time", element);
                String endTime = getTagValue("end_time", element);
                String status = getTagValue("status", element);

                System.out.println("Parsed Reservation ID: " + reservationId);

                reservations.add(new ApprovalReservation(
                        reservationId, userId, terminalId, roomId,
                        reservationDate, startTime, endTime, status
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("XML Parsing Error: " + e.getMessage());
        }

        return reservations;
    }

    // Helper method to safely get tag values
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0 && nodeList.item(0) != null) {
            return nodeList.item(0).getTextContent().trim();  // Trim to avoid whitespace issues
        }
        return "UNKNOWN";  // Return placeholder if missing
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(
                null, message, "Server Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}