package client.student.controller;

import client.student.view.TerminalItemCardView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class TerminalPickerWindowController {

    public void loadTerminals() throws IOException {
        // This would be the list of terminals from your XML (you should parse it here)
        NodeList terminals = getTerminalNodeList();  // Assuming this method gets the XML data
        for (int i = 0; i < terminals.getLength(); i++) {
            Element terminalElement = (Element) terminals.item(i);
            String terminalId = terminalElement.getElementsByTagName("terminal_id").item(0).getTextContent();
            String terminalOS = terminalElement.getElementsByTagName("terminal_os").item(0).getTextContent();
            String terminalStatus = terminalElement.getElementsByTagName("terminal_status").item(0).getTextContent();

            // Get the current day
            Calendar calendar = Calendar.getInstance();
            String currentDay = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

            // Find available time for today
            String availableTime = getAvailableTimeForDay(terminalElement, currentDay);

            // Get today's date
            String currentDate = getCurrentDate();

            // Create and load the Terminal Item Card View
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/TerminalItemCardView.fxml"));
            HBox terminalItemCard = loader.load();
            TerminalItemCardView cardController = loader.getController();
            cardController.setTerminalCardData(terminalId, availableTime, currentDate, terminalOS);

            // Add the card to the UI (assuming you have a parent container)
            // parentContainer.getChildren().add(terminalItemCard);
        }
    }

    private NodeList getTerminalNodeList() {
        try {
            // Load the XML document
            File xmlFile = new File("path/to/your/terminals.xml");  // Path to your XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // Normalize the XML structure
            document.getDocumentElement().normalize();

            // Get all terminal nodes
            NodeList terminalNodes = document.getElementsByTagName("Terminal");

            return terminalNodes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null in case of error
        }
    }
    // Helper to map calendar day to string
    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            case Calendar.SUNDAY: return "Sunday";
            default: return "Unknown";
        }
    }

    // Helper to get available time for the current day
    private String getAvailableTimeForDay(Element terminalElement, String day) {
        NodeList days = terminalElement.getElementsByTagName("default_schedule");
        for (int i = 0; i < days.getLength(); i++) {
            Element schedule = (Element) days.item(i);
            String time = schedule.getElementsByTagName(day).item(0).getTextContent();
            if (time != null && !time.equals("Closed")) {
                return time;
            }
        }
        return "Not Available";
    }

    // Helper to get current date in a specific format
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return String.format("%02d/%02d/%d", day, month, year);
    }
}
