package server.student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Map;

public class ModifyReservationProcessor {
    private final String filePath;
    private final String studentID;

    public ModifyReservationProcessor(String studentID) {
        this.studentID = studentID;
        this.filePath = "/src/main/java/server/util/reserved.xml";
    }

    public String fetchReservations() {
        try {
            Document doc = loadReservations();
            filterReservations(doc);
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
                String currentStudentId = reservation.getElementsByTagName("Student_ID")
                        .item(0).getTextContent();
                String currentResId = reservation.getElementsByTagName("reservation_id")
                        .item(0).getTextContent();

                if (currentResId.equals(reservationId) && currentStudentId.equals(studentID)) {
                    updateReservationFields(reservation, updates);
                    saveReservations(doc);
                    return successResponse("Reservation updated successfully");
                }
            }
            return errorResponse("Reservation not found or access denied");
        } catch (Exception e) {
            return errorResponse("Error updating reservation");
        }
    }

    private Document loadReservations() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }

    private void filterReservations(Document doc) {
        NodeList reservations = doc.getElementsByTagName("Reservation");
        NodeList allReservations = doc.getElementsByTagName("Reservation");

        for (int i = allReservations.getLength() - 1; i >= 0; i--) {
            Element reservation = (Element) allReservations.item(i);
            String reservationStudentId = reservation.getElementsByTagName("Student_ID")
                    .item(0).getTextContent();
            if (!reservationStudentId.equals(studentID)) {
                reservation.getParentNode().removeChild(reservation);
            }
        }
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
        transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
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
}