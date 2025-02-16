package server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.utility.LogReport;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReportGeneratorProcessor {
    private static final String filePath = "src/main/java/server/util/logs.xml";

    // Parse the log XML and return the list of LogReport objects
    public static List<LogReport> parseLogXML() {
        List<LogReport> logs = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            NodeList logNodes = document.getElementsByTagName("Log");
            for (int i = 0; i < logNodes.getLength(); i++) {
                Node logNode = logNodes.item(i);
                if (logNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element logElement = (Element) logNode;
                    String userID = logElement.getElementsByTagName("UserID").item(0).getTextContent();
                    String userType = logElement.getElementsByTagName("UserType").item(0).getTextContent();
                    String action = logElement.getElementsByTagName("Action").item(0).getTextContent();
                    String date = logElement.getElementsByTagName("Date").item(0).getTextContent();
                    String time = logElement.getElementsByTagName("Time").item(0).getTextContent();

                    logs.add(new LogReport(userID, userType, action, date, time));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }

    // Apply sorting and filtering on the logs
    public static List<LogReport> applySortingAndFiltering(List<LogReport> logs, String sortOption, String dateFilter) {
        if (logs == null || sortOption == null) return logs;

        List<LogReport> filteredLogs = new ArrayList<>(logs);

        switch (sortOption) {
            case "Sort by Students":
                filteredLogs.removeIf(log -> !log.getUserType().equals("Student"));
                filteredLogs.sort(Comparator.comparing(LogReport::getUserID)); // Sort by UserID for Students
                break;
            case "Sort by Admin":
                filteredLogs.removeIf(log -> !log.getUserType().equals("Admin"));
                filteredLogs.sort(Comparator.comparing(LogReport::getUserID)); // Sort by UserID for Admin
                break;
            case "Filter by Date":
                if (dateFilter != null && !dateFilter.isEmpty()) {
                    filteredLogs.removeIf(log -> !log.getDate().equals(dateFilter));
                } else {
                    // Invalid date input
                    throw new IllegalArgumentException("Inputted date is invalid.");
                }
                break;
            default:
                // No sorting or filtering applied
                break;
        }
        return filteredLogs;
    }
}
