// File: client/admin/model/ReservationApprovalModel.java
package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.*;
import server.utility.ApprovalTerminal;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservationApprovalModel {
    private ServerConnection serverConnection;

    // Constructor: Get server connection
    public ReservationApprovalModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server connection failed.");
        }
    }

    /** Fetch approval terminals from server */
    public List<ApprovalTerminal> fetchApprovalTerminals() {
        List<ApprovalTerminal> terminals = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return terminals;
        }

        try {
            // Request to server
            serverConnection.sendMessage("<Request><Type>ViewApprovalTerminals</Type></Request>");
            String responseXML = serverConnection.readMessage();
            terminals = parseXMLResponse(responseXML);
        } catch (IOException e) {
            showErrorDialog("Failed to fetch terminals.");
        }
        return terminals;
    }

    /** Parse XML response into ApprovalTerminal objects */
    private List<ApprovalTerminal> parseXMLResponse(String xmlResponse) {
        List<ApprovalTerminal> terminals = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("Terminal");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String terminalRoom = element.getElementsByTagName("terminal_room").item(0).getTextContent();
                String terminalStatus = element.getElementsByTagName("terminal_status").item(0).getTextContent();
                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String userId = element.getElementsByTagName("user_id").item(0).getTextContent();
                String reservationDate = element.getElementsByTagName("reservation_date").item(0).getTextContent();

                terminals.add(new ApprovalTerminal(terminalId, terminalRoom, terminalStatus,
                        reservationId, userId, reservationDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terminals;
    }

    /** Show error dialog */
    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE));
    }
}
