package server.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLUtility {

    private static final String FILE_PATH = "src/main/java/server/util/reserved.xml";

    public static List<StudentReservation> loadStudentReservationsFromXML() {
        List<StudentReservation> reservations = new ArrayList<>();

        try {
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            NodeList nodeList = document.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String terminalRoom = element.getElementsByTagName("terminal_room").item(0).getTextContent();
                String status = element.getElementsByTagName("status").item(0).getTextContent();

                reservations.add(new StudentReservation(reservationId, terminalId, terminalRoom, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
