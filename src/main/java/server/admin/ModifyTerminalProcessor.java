package server.admin;

import org.w3c.dom.*;
import server.utility.Terminal;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModifyTerminalProcessor {

    // Method to parse XML and return a list of Terminal objects
    public static List<Terminal> parseXML(String filePath) {
        List<Terminal> terminals = new ArrayList<>();

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
            NodeList terminalNodes = document.getElementsByTagName("Terminal");

            // Loop through the nodes and extract data
            for (int i = 0; i < terminalNodes.getLength(); i++) {
                Node node = terminalNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get the data for each terminal
                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOs = getTagValue("terminal_os", element);
                    String terminalStatus = getTagValue("terminal_status", element);

                    // Create a new Terminal object and add it to the list
                    Terminal terminal = new Terminal(terminalId, terminalRoom, terminalOs, terminalStatus);
                    terminals.add(terminal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return terminals;
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
