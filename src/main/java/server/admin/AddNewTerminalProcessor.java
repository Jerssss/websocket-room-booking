package server.admin;

import client.admin.model.AddNewTerminalModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class AddNewTerminalProcessor {

    private final String filePath = "src/main/java/server/util/terminal.xml"; // Replace with your actual path

    public boolean processTerminalData(AddNewTerminalModel terminalModel) {
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.err.println("Error: terminal.xml file not found at " + filePath);
                return false;
            }

            // Parse the existing XML file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true); // Ignore whitespace during parsing
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Get the root element
            Element root = doc.getDocumentElement(); // This is <Terminals>

            // Create a new Terminal element
            Element terminal = doc.createElement("Terminal");

            Element terminalId = doc.createElement("terminal_id");
            terminalId.setTextContent(terminalModel.getTerminalId().trim());
            terminal.appendChild(terminalId);

            Element terminalRoom = doc.createElement("terminal_room");
            terminalRoom.setTextContent(terminalModel.getRoom().trim());
            terminal.appendChild(terminalRoom);

            Element terminalOs = doc.createElement("terminal_os");
            terminalOs.setTextContent(terminalModel.getOsType().trim());
            terminal.appendChild(terminalOs);

            Element terminalStatus = doc.createElement("terminal_status");
            terminalStatus.setTextContent(terminalModel.getStatus().trim());
            terminal.appendChild(terminalStatus);

            // Append the new terminal to the root element
            root.appendChild(terminal);

            // Write the updated document back to the file with precise formatting
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Set output properties for clean formatting
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Remove unnecessary whitespace nodes before writing
            removeWhitespaceNodes(doc);

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to remove whitespace nodes from the document
    private void removeWhitespaceNodes(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhitespaceNodes(child);
            }
        }
    }
}