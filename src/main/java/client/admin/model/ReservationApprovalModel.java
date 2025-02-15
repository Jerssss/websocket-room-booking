package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.*;
import server.utility.StudentReservation;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    public List<StudentReservation> fetchApprovalReservations() {
        List<StudentReservation> reservations = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return reservations;
        }

        try {
            serverConnection.sendMessage("<Request><Type>ViewApprovalReservations</Type></Request>");
            String responseXML = serverConnection.readMessage();
            reservations = parseXMLResponse(responseXML);
        } catch (IOException e) {
            showErrorDialog("Failed to fetch reservations.");
        }
        return reservations;
    }

    private List<StudentReservation> parseXMLResponse(String xmlResponse) {
        List<StudentReservation> reservations = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String terminalRoom = element.getElementsByTagName("terminal_room").item(0).getTextContent();
                String date = element.getElementsByTagName("date").item(0).getTextContent();
                String status = element.getElementsByTagName("terminal_status").item(0).getTextContent();

                reservations.add(new StudentReservation(reservationId, terminalId, terminalRoom, date, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE));
    }
}
