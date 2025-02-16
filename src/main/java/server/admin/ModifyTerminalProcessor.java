package server.admin;

import org.w3c.dom.*;
import server.utility.Terminal;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

                    terminals.add(new Terminal(terminalId, terminalRoom, terminalOs, terminalStatus));
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
