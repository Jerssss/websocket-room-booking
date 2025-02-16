// File: client/admin/model/ReservationApprovalModel.java
package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import server.utility.ApprovalReservation;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservationApprovalModel {

    private ServerConnection serverConnection;
    private static final String XML_FILE_PATH = "src/main/java/server/util/reservations.xml";

    public ReservationApprovalModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            System.err.println("Server connection failed: " + e.getMessage());
        }
    }

    public List<ApprovalReservation> getReservations() {
        return loadReservations();  // Call the existing loadReservations() method
    }

    // Existing method to parse reservations from XML
    public List<ApprovalReservation> loadReservations() {
        List<ApprovalReservation> reservations = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(XML_FILE_PATH));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Reservation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String reservationId = getTagValue("reservation_id", element);
                    String userId = getTagValue("user_id", element);
                    String terminalId = getTagValue("terminal_id", element);
                    String roomId = getTagValue("room_id", element);
                    String reservationDate = getTagValue("reservation_date", element);
                    String startTime = getTagValue("start_time", element);
                    String endTime = getTagValue("end_time", element);
                    String status = getTagValue("status", element);

                    reservations.add(new ApprovalReservation(
                            reservationId, userId, terminalId, roomId,
                            reservationDate, startTime, endTime, status
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Helper method to get XML tag values
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
