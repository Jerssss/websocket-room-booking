// File: server/utility/ReservationApprovalProcessor.java
package server.utility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReservationApprovalProcessor {

    private static final String XML_FILE_PATH = "C:/Users/krist/IdeaProjects/9444-team1_preproject/src/main/java/server/util/reservationapproval.xml";

    public static List<ApprovalReservation> readReservationsFromXML(String filePath) {
        List<ApprovalReservation> reservations = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
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

    // Helper method to extract tag values
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}

