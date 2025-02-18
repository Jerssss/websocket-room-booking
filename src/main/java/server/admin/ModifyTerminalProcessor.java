package server.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import server.utility.Terminal;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyTerminalProcessor {
    private static final String filePath = "src/main/java/server/util/terminal.xml";

    public static List<Terminal> parseXML() {
        List<Terminal> terminals = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            document.getDocumentElement().normalize();
            NodeList terminalNodes = document.getElementsByTagName("Terminal");

            for (int i = 0; i < terminalNodes.getLength(); i++) {
                Node node = terminalNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalOs = getTagValue("terminal_os", element);
                    String terminalStatus = getTagValue("terminal_status", element);
                    String date = getTagValue("reservation_date", element);
                    String startTime = getTagValue("start_time", element);
                    String endTime = getTagValue("end_time", element);


                    terminals.add(new Terminal(terminalId, terminalRoom, terminalOs, terminalStatus, date, startTime, endTime));
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

    public static ObservableList<Terminal> searchTerminals(ObservableList<Terminal> terminalData, String searchText) {

        if (searchText == null || searchText.trim().isEmpty()) {
            System.out.println("[DEBUG] Search text is empty. Resetting to full terminal list.");
            return terminalData;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        ObservableList<Terminal> filteredList = terminalData.stream()
                .filter(terminal ->
                        terminal.getTerminalRoom().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalId().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalOs().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getTerminalStatus().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getReservationDate().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getStartTime().toLowerCase().contains(lowerCaseSearchText) ||
                                terminal.getEndTime().toLowerCase().contains(lowerCaseSearchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        System.out.println("[DEBUG] Search completed. Matching results: " + filteredList.size());
        return filteredList;
    }

    public static void saveToXML(List<Terminal> terminals) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("Terminals");
            document.appendChild(root);

            for (Terminal terminal : terminals) {
                Element terminalElement = document.createElement("Terminal");
                root.appendChild(terminalElement);

                appendChildWithText(document, terminalElement, "terminal_id", terminal.getTerminalId());
                appendChildWithText(document, terminalElement, "terminal_room", terminal.getTerminalRoom());
                appendChildWithText(document, terminalElement, "terminal_os", terminal.getTerminalOs());
                appendChildWithText(document, terminalElement, "terminal_status", terminal.getTerminalStatus());
                appendChildWithText(document, terminalElement, "reservation_date", terminal.getReservationDate());
                appendChildWithText(document, terminalElement, "start_time", terminal.getStartTime());
                appendChildWithText(document, terminalElement, "end_time", terminal.getEndTime());
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void appendChildWithText(Document doc, Element parent, String tag, String text) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(text));
        parent.appendChild(element);
    }
}
