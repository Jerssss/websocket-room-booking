package server.admin;


import server.utility.StudentReservation;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ViewStudentReservationsProcessor {


    private static final String FILE_PATH = "src/main/java/server/util/reserved.xml";


    // Load student reservations from XML file
    public static List<StudentReservation> loadStudentReservationsFromXML() {
        List<StudentReservation> studentReservations = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.out.println("File not found: " + FILE_PATH);
                return studentReservations;
            }


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);


            NodeList nodeList = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String reservationId = getTagValue("reservation_id", element);
                String terminalId = getTagValue("terminal_id", element);
                String terminalRoom = getTagValue("terminal_room", element);
                String date = getTagValue("date", element);
                String terminalStatus = getTagValue("terminal_status", element);


                studentReservations.add(new StudentReservation(reservationId, terminalId, terminalRoom, date, terminalStatus));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentReservations;
    }


    // Helper method to get the value of a tag
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}
