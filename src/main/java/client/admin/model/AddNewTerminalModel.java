package client.admin.model;

import client.utility.ServerConnection;
import javafx.application.Platform;
import org.w3c.dom.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.IOException;

public class AddNewTerminalModel {
    private ServerConnection serverConnection;

    public AddNewTerminalModel() {
        try {
            serverConnection = new ServerConnection(); // Reuse ServerConnection class
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public boolean sendTerminalData(String terminalId, String room, String osType, String status) {
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return false;
        }
        try {
            String requestXML = createXMLRequest(terminalId, room, osType, status);
            serverConnection.sendMessage(requestXML); // Use ServerConnection to send data
            String responseXML = serverConnection.readMessage(); // Read server response
            return parseXMLResponse(responseXML);
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
            return false;
        }
    }

    private String createXMLRequest(String terminalId, String room, String osType, String status)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("AddTerminal");
        doc.appendChild(root);

        Element id = doc.createElement("TerminalID");
        id.appendChild(doc.createTextNode(terminalId));
        root.appendChild(id);

        Element roomElement = doc.createElement("Room");
        roomElement.appendChild(doc.createTextNode(room));
        root.appendChild(roomElement);

        Element os = doc.createElement("OSType");
        os.appendChild(doc.createTextNode(osType));
        root.appendChild(os);

        Element statusElement = doc.createElement("Status");
        statusElement.appendChild(doc.createTextNode(status));
        root.appendChild(statusElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    private boolean parseXMLResponse(String xmlResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            Element root = doc.getDocumentElement();
            return root.getElementsByTagName("Status").item(0).getTextContent().equalsIgnoreCase("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
