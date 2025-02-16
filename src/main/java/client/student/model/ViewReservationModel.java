package client.student.model;

import client.utility.ServerConnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.*;
import server.utility.Reservation;

import javax.swing.*;
import javax.xml.parsers.*;


public class ViewReservationModel {
    private  ServerConnection serverConnection;

    public ViewReservationModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    // Fetch reservations from the server and return as a Map
    public List<Reservation> fetchAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return reservations;
        }

        try {
            // Sending request to fetch all reservations
            System.out.println("Sending request to fetch reservations...");
            serverConnection.sendMessage("<Request><Type>fetch_reservation</Type></Request>");
            String responseXML = serverConnection.readMessage();

            // Parse the XML response
            System.out.println("Received response from server...");
            reservations = parseXMLResponse(responseXML);

            // Get logged-in user's ID
            String loggedInUserId = serverConnection.getLoggedInUserId();
            System.out.println("Logged-in User ID: " + loggedInUserId); // Debugging logged-in user ID

            // Filter reservations by the logged-in user's ID
            List<Reservation> filteredReservations = new ArrayList<>();
            for (Reservation res : reservations) {
                System.out.println("Checking reservation with User ID: " + res.getUserId()); // Debugging each reservation's user ID

                // Only add reservations that match the logged-in user's ID
                if (res.getUserId().equals(loggedInUserId)) {
                    filteredReservations.add(res);
                }
            }

            // Debugging the results
            System.out.println("Total reservations found: " + reservations.size()); // Debugging the total fetched reservations
            System.out.println("Filtered reservations count: " + filteredReservations.size()); // Debugging the filtered reservations count

            return filteredReservations; // Return only the filtered list

        } catch (IOException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
            e.printStackTrace(); // Log the exception details
        }
        return reservations;
    }

    private List<Reservation> parseXMLResponse(String xmlResponse) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("Reservation");

            // Debugging the number of reservations found in XML
            System.out.println("Parsing XML Response... Found " + nodeList.getLength() + " reservations.");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String userId = element.getElementsByTagName("user_id").item(0).getTextContent();
                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String reservationDate = element.getElementsByTagName("reservation_date").item(0).getTextContent();
                String startTime = element.getElementsByTagName("start_time").item(0).getTextContent();
                String endTime = element.getElementsByTagName("end_time").item(0).getTextContent();
                String status = element.getElementsByTagName("status").item(0).getTextContent();

                // Debugging reservation data
                System.out.println("Parsing reservation: ID=" + reservationId + ", UserID=" + userId + ", Status=" + status);

                reservations.add(new Reservation(reservationId, userId, terminalId, reservationDate, startTime, endTime, status));
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log any parsing errors
        }
        return reservations;
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(
                null, message, "Connection Error", JOptionPane.ERROR_MESSAGE
        ));

    }
}
