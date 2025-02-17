package client.student.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import server.student.CreateReservationProcessor;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class CreateReservationModel {
    private ServerConnection serverConnection;
    private final CreateReservationProcessor processor;


    public CreateReservationModel() {
        this.processor = new CreateReservationProcessor();
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public boolean sendReservationData(String terminalId, String name, String room, String osType) {
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return false;
        }
        try {
            String requestXML = createXMLRequest(terminalId, name, room, osType);
            serverConnection.sendMessage(requestXML);
            String responseXML = serverConnection.readMessage();
            return parseXMLResponse(responseXML);
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
            return false;
        }
    }

    private String createXMLRequest(String terminalId, String name, String room, String osType)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("CreateReservation");
        doc.appendChild(root);

        Element id = doc.createElement("TerminalID");
        id.appendChild(doc.createTextNode(terminalId));
        root.appendChild(id);

        Element nameElement = doc.createElement("Name");
        nameElement.appendChild(doc.createTextNode(name));
        root.appendChild(nameElement);

        Element roomElement = doc.createElement("Room");
        roomElement.appendChild(doc.createTextNode(room));
        root.appendChild(roomElement);

        Element os = doc.createElement("OSType");
        os.appendChild(doc.createTextNode(osType));
        root.appendChild(os);

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
            return root.getElementsByTagName("OSType").item(0).getTextContent().equalsIgnoreCase("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
