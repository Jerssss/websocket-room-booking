// File: client/admin/model/ReservationApprovalModel.java
package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import server.utility.ApprovalReservation;

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

    public List<ApprovalReservation> fetchAllApprovalReservations() {
        List<ApprovalReservation> reservations = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return reservations;
        }

        try {
            serverConnection.sendMessage("<Request><Type>ViewReservationApprovals</Type></Request>");
            String responseXML = serverConnection.readMessage();
            reservations = parseXMLResponse(responseXML);
        } catch (IOException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
        }

        return reservations;
    }

    private List<ApprovalReservation> parseXMLResponse(String xmlResponse) {
        List<ApprovalReservation> reservations = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("ApprovalReservation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String userId = element.getElementsByTagName("user_id").item(0).getTextContent();
                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String roomId = element.getElementsByTagName("room_id").item(0).getTextContent();
                String reservationDate = element.getElementsByTagName("reservation_date").item(0).getTextContent();
                String startTime = element.getElementsByTagName("start_time").item(0).getTextContent();
                String endTime = element.getElementsByTagName("end_time").item(0).getTextContent();
                String status = element.getElementsByTagName("status").item(0).getTextContent();

                reservations.add(new ApprovalReservation(
                        reservationId, userId, terminalId, roomId,
                        reservationDate, startTime, endTime, status
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(
                null, message, "Server Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}
