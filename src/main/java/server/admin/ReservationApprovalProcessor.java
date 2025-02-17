// File: server/admin/ReservationApprovalProcessor.java
package server.admin;

import org.w3c.dom.*;
import server.utility.ApprovalReservation;
import server.utility.Terminal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ReservationApprovalProcessor {

    private static final String FILE_PATH = "src/main/java/server/util/reservation_approval.xml";

    // Parse XML file and return a list of ApprovalReservation objects
    public static List<ApprovalReservation> parseXML() {
        List<ApprovalReservation> reservations = new ArrayList<>();
        try {
            System.out.println("Loading XML file from: " + FILE_PATH); // Debug print

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(FILE_PATH));
            document.getDocumentElement().normalize();

            NodeList reservationNodes = document.getElementsByTagName("Reservation");
            System.out.println("Found " + reservationNodes.getLength() + " reservations."); // Debug print

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

    // Helper method to get tag value
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    public static void saveToXML(List<ApprovalReservation> reservations) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("Terminals");
            document.appendChild(root);

            for (ApprovalReservation reservation : reservations) {
                Element terminalElement = document.createElement("Terminal");
                root.appendChild(terminalElement);

                appendChildWithText(document, terminalElement, "reservation_id", reservation.getTerminalId());
                appendChildWithText(document, terminalElement, "user_id", reservation.getUserId());
                appendChildWithText(document, terminalElement, "terminal_id", reservation.getTerminalId());
                appendChildWithText(document, terminalElement, "room_id", reservation.getRoomId());
                appendChildWithText(document, terminalElement, "reservation_date", reservation.getReservationDate());
                appendChildWithText(document, terminalElement, "start_time", reservation.getStartTime());
                appendChildWithText(document, terminalElement, "end_time", reservation.getEndTime());
                appendChildWithText(document, terminalElement, "status", reservation.getStatus());
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(FILE_PATH)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void appendChildWithText(Document doc, Element parent, String tag, String text) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(text));
        parent.appendChild(element);
    }

    // Success response
    private String successResponse(String message) {
        return "<Response><Status>SUCCESS</Status><Message>" + message + "</Message></Response>";
    }

    // Error response
    private String errorResponse(String message) {
        return "<Response><Status>ERROR</Status><Message>" + message + "</Message></Response>";
    }

    // Load reservations XML file
    private Document loadReservations() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(FILE_PATH));
    }
}
