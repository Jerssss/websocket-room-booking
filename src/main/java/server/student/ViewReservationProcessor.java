// server/student/ViewReservationProcessor.java
package server.student;

import server.utility.Reservation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import client.utility.SessionManager;

public class ViewReservationProcessor {
    private static final String FILE_PATH = "src/main/java/server/util/reservation_approval.xml";

    public static List<Reservation> loadReservationFromXML(String sessionToken) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            // Get the user_id from the sessionToken
            String currentUserId = SessionManager.getUserId(sessionToken);
            if (currentUserId == null) {
                System.out.println("Invalid session token. User not logged in.");
                return reservations;
            }

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.out.println("File not found: " + FILE_PATH);
                return reservations;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodeList = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String userId = getTagValue("user_id", element);

                // Filter reservations by the current user's ID
                if (userId != null && userId.equals(currentUserId)) {
                    String reservationId = getTagValue("reservation_id", element);
                    String terminalNumber = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("room_id", element);
                    String date = getTagValue("reservation_date", element);
                    String startTime = getTagValue("start_time", element);
                    String endTime = getTagValue("end_time", element);
                    String terminalStatus = getTagValue("status", element);

                    reservations.add(new Reservation(reservationId, userId, terminalNumber, terminalRoom, date, startTime, endTime, terminalStatus));
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
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}