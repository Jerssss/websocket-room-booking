// File: server/admin/ReservationApprovalProcessor.java
package server.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import server.utility.StudentReservation;

public class ReservationApprovalProcessor {
    private static final String FILE_PATH = "src/main/java/server/util/reservations.xml";

    // Load all reservations from the XML file
    public ObservableList<StudentReservation> loadAllReservations() {
        ObservableList<StudentReservation> reservations = FXCollections.observableArrayList();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return reservations;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String resId = getTagValue("reservation_id", element);
                    String terminalId = getTagValue("terminal_id", element);
                    String room = getTagValue("terminal_room", element);
                    String date = getTagValue("date", element);
                    String status = getTagValue("terminal_status", element);

                    reservations.add(new StudentReservation(resId, terminalId, room, date, status));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
