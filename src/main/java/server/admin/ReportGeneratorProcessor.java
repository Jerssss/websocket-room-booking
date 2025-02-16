package server.admin;

import server.utility.LogReport;
import server.utility.ReservationReport;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGeneratorProcessor {

    // Search Logs
    public static List<LogReport> searchLogs(String query, List<LogReport> logs) {
        if (query.isEmpty()) return logs;
        String lower = query.toLowerCase();
        return logs.stream().filter(log ->
                        log.getUserID().toLowerCase().contains(lower) ||
                                log.getUserType().toLowerCase().contains(lower) ||
                                log.getAction().toLowerCase().contains(lower) ||
                                log.getDate().toLowerCase().contains(lower) ||
                                log.getTime().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // Search Reservations
    public static List<ReservationReport> searchReservations(String query, List<ReservationReport> reservations) {
        if (query.isEmpty()) return reservations;
        String lower = query.toLowerCase();
        return reservations.stream().filter(res ->
                        res.getReservationId().toLowerCase().contains(lower) ||
                                res.getTerminalId().toLowerCase().contains(lower) ||
                                res.getRoomNumber().toLowerCase().contains(lower) ||
                                res.getDate().toLowerCase().contains(lower) ||
                                res.getStatus().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // Parse Logs
    public static List<LogReport> parseLogXML() {
        List<LogReport> logs = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/main/java/server/util/logs.xml"));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Log");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                logs.add(new LogReport(
                        getElementText(element, "UserID"),
                        getElementText(element, "UserType"),
                        getElementText(element, "Action"),
                        getElementText(element, "Date"),
                        getElementText(element, "Time")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }

    // Parse Reservations
    public static List<ReservationReport> parseReservationXML() {
        List<ReservationReport> reservations = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/main/java/server/util/reservation_approval.xml"));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Reservation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                // Extracting values with updated tag names
                String reservationId = getElementText(element, "reservation_id");
                String userId = getElementText(element, "user_id");
                String terminalId = getElementText(element, "terminal_id");
                String roomId = getElementText(element, "room_id"); // Updated from roomNumber
                String reservationDate = getElementText(element, "reservation_date"); // Updated from date
                String startTime = getElementText(element, "start_time");
                String endTime = getElementText(element, "end_time");
                String status = getElementText(element, "status");

                // Ensure all values are present before adding
                if (reservationId.isEmpty() || userId.isEmpty() || terminalId.isEmpty()) {
                    System.out.println("⚠ WARNING: Some fields are empty. Skipping entry.");
                    continue;
                }

                reservations.add(new ReservationReport(reservationId, userId, terminalId, roomId, status, reservationDate, startTime, endTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Utility: Get text from XML element safely
    private static String getElementText(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return "";
    }
}
