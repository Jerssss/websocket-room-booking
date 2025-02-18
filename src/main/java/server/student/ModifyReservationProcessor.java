package server.student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.utility.Reservation;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModifyReservationProcessor {
    private static final String filePath = "src/main/java/server/util/reservation_approval.xml";

    public static void updateReservation(Reservation updatedReservation) {
        List<Reservation> allReservations = parseXML();

        // Find and update the reservation
        for (Reservation res : allReservations) {
            if (res.getReservationId().equals(updatedReservation.getReservationId())) {
                res.setStatus(updatedReservation.getStatus());
                res.setDate(updatedReservation.getDate());
                res.setStartTime(updatedReservation.getStartTime());
                res.setEndTime(updatedReservation.getEndTime());
                break;
            }
        }

        saveToXML(allReservations); // Save the entire updated list
    }

    // Add this method to delete a single reservation
    public static void deleteReservation(String reservationId) {
        List<Reservation> allReservations = parseXML();
        allReservations.removeIf(res -> res.getReservationId().equals(reservationId));
        saveToXML(allReservations);
    }

    public static List<Reservation> parseXML() {
        List<Reservation> reservations = new ArrayList<>();

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
                    String userId = getTagValue("user_id", element); // Add this line
                    String terminalNumber = getTagValue("terminal_id", element);
                    String roomNumber = getTagValue("room_id", element);
                    String date = getTagValue("reservation_date", element);
                    String startTime = getTagValue("start_time", element);
                    String endTime = getTagValue("end_time", element);
                    String status = getTagValue("status", element);

                    reservations.add(new Reservation(reservationId, userId, terminalNumber, roomNumber, date, startTime, endTime, status));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Helper method to extract the value of a tag
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }


    public static void saveToXML(List<Reservation> reservations) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("Reservations");
            document.appendChild(root);

            for (Reservation reservation : reservations) {
                Element reservationElement = document.createElement("Reservation");
                root.appendChild(reservationElement);

                // Ensure tags match those used in parseXML
                appendChildWithText(document, reservationElement, "reservation_id", reservation.getReservationId());
                appendChildWithText(document, reservationElement, "user_id", reservation.getUserId());
                appendChildWithText(document, reservationElement, "terminal_id", reservation.getTerminalNumber());
                appendChildWithText(document, reservationElement, "room_id", reservation.getRoomNumber());
                appendChildWithText(document, reservationElement, "reservation_date", reservation.getDate());
                appendChildWithText(document, reservationElement, "start_time", reservation.getStartTime());
                appendChildWithText(document, reservationElement, "end_time", reservation.getEndTime());
                appendChildWithText(document, reservationElement, "status", reservation.getStatus());
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void appendChildWithText(Document doc, Element parent, String tag, String text) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(text));
        parent.appendChild(element);
    }
}