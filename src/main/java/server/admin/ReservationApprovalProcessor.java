// File: server/admin/ReservationApprovalProcessor.java
package server.admin;

import org.w3c.dom.*;
import server.utility.ApprovalReservation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationApprovalProcessor {

    private static final String FILE_PATH = "src/main/java/server/util/reservationapproval.xml";

    /** Parse XML file and return a list of ApprovalReservation objects */
    public static List<ApprovalReservation> parseXML(String filePath) {
        List<ApprovalReservation> reservations = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            document.getDocumentElement().normalize();

            NodeList reservationNodes = document.getElementsByTagName("Reservation");

            for (int i = 0; i < reservationNodes.getLength(); i++) {
                Node node = reservationNodes.item(i);
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

                    ApprovalReservation reservation = new ApprovalReservation(
                            reservationId, userId, terminalId, roomId,
                            reservationDate, startTime, endTime, status
                    );
                    reservations.add(reservation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    // === Uncomment and complete the additional operations if needed === //

    /*
    public String fetchAllReservations() {
        try {
            Document doc = loadReservations();
            return convertDocToString(doc);
        } catch (Exception e) {
            return errorResponse("Error fetching reservations");
        }
    }

    public String updateReservation(String reservationId, Map<String, String> updates) {
        try {
            Document doc = loadReservations();
            NodeList reservations = doc.getElementsByTagName("Reservation");

            for (int i = 0; i < reservations.getLength(); i++) {
                Element reservation = (Element) reservations.item(i);
                String currentResId = reservation.getElementsByTagName("reservation_id")
                        .item(0).getTextContent();

                if (currentResId.equals(reservationId)) {
                    updateReservationFields(reservation, updates);
                    saveReservations(doc);
                    return successResponse("Reservation updated successfully");
                }
            }
            return errorResponse("Reservation not found");
        } catch (Exception e) {
            return errorResponse("Error updating reservation");
        }
    }

    private Document loadReservations() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(FILE_PATH));
    }

    private void updateReservationFields(Element reservation, Map<String, String> updates) {
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            NodeList nodes = reservation.getElementsByTagName(entry.getKey());
            if (nodes.getLength() > 0) {
                nodes.item(0).setTextContent(entry.getValue());
            }
        }
    }

    private void saveReservations(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(FILE_PATH)));
    }

    private String convertDocToString(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    private String successResponse(String message) {
        return "<Response><Status>SUCCESS</Status><Message>" + message + "</Message></Response>";
    }

    private String errorResponse(String message) {
        return "<Response><Status>ERROR</Status><Message>" + message + "</Message></Response>";
    }
    */
}
