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
        List<Reservation>  reservation = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return reservation;
        }
        try {
            serverConnection.sendMessage("<Request><Type>fetch_reservation</Type></Request>"); // XML request
            String responseXML = serverConnection.readMessage();
            reservation = parseXMLResponse(responseXML);
        } catch (IOException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
        }
        return reservation;
    }

    private List<Reservation> parseXMLResponse(String xmlResponse) {
        List<Reservation> reservation = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("Reservation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String userId = element.getElementsByTagName("user_id").item(0).getTextContent();
                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String reservationDate = element.getElementsByTagName("reservation_date").item(0).getTextContent();
                String startTime = element.getElementsByTagName("start_time").item(0).getTextContent();
                String endTime = element.getElementsByTagName("end_time").item(0).getTextContent();
                String status = element.getElementsByTagName("status").item(0).getTextContent();

                reservation.add(new Reservation(reservationId, userId, terminalId, reservationDate, startTime, endTime, status));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservation;
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));

    }


}
