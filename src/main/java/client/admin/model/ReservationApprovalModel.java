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
import java.io.InputStream;
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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(is);

            NodeList reservationNodes = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < reservationNodes.getLength(); i++) {
                Element reservationElement = (Element) reservationNodes.item(i);

                String reservationId = reservationElement.getElementsByTagName("reservation_id").item(0).getTextContent();
                String userId = reservationElement.getElementsByTagName("user_id").item(0).getTextContent();
                String terminalId = reservationElement.getElementsByTagName("terminal_id").item(0).getTextContent();
                String roomNumber = reservationElement.getElementsByTagName("room_number").item(0).getTextContent();
                String reservationDate = reservationElement.getElementsByTagName("reservation_date").item(0).getTextContent();
                String startTime = reservationElement.getElementsByTagName("start_time").item(0).getTextContent();
                String endTime = reservationElement.getElementsByTagName("end_time").item(0).getTextContent();
                String status = reservationElement.getElementsByTagName("status").item(0).getTextContent();

                ApprovalReservation reservation = new ApprovalReservation(reservationId, userId, terminalId, roomNumber,
                        reservationDate, startTime, endTime, status);
                reservations.add(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
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