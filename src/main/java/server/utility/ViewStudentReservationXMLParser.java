package server.utility;

import server.utility.StudentReservation;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ViewStudentReservationXMLParser{

    // Method to parse XML and return a list of Terminal objects
    public static List<StudentReservation> parseXML(String filePath) {
        List<StudentReservation> studentReservations = new ArrayList<>();

        try {
            // Initialize DocumentBuilderFactory and DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file
            File xmlFile = new File(filePath);
            Document document = builder.parse(xmlFile);

            // Normalize the XML structure
            document.getDocumentElement().normalize();

            // Get all <Terminal> nodes
            NodeList studentNodes = document.getElementsByTagName("Reservation");

            // Loop through the nodes and extract data
            for (int i = 0; i < studentNodes.getLength(); i++) {
                Node node = studentNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get the data for each terminal
                    String reservationId = getTagValue("reservation_id", element);
                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String date = getTagValue("date", element);
                    String terminalStatus = getTagValue("terminal_status", element);

                    // Create a new Terminal object and add it to the list
                    StudentReservation terminal = new StudentReservation(reservationId, terminalId, terminalRoom, date, terminalStatus);
                    studentReservations.add(terminal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentReservations;
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
}