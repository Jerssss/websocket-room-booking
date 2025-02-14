package client.admin.model;

import client.utility.ServerConnection;
import javafx.application.Platform;
import org.w3c.dom.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import server.utility.StudentReservation;
import server.utility.XMLUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ViewStudentReservationsModel {
    private ServerConnection serverConnection;

    public ViewStudentReservationsModel() {
        try {
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public List<StudentReservation> fetchStudentReservations() {
        List<StudentReservation> studentReservations = new ArrayList<>();
        if (serverConnection == null) {
            showErrorDialog("No server connection available.");
            return studentReservations;
        }
        try {
            serverConnection.sendMessage("<Request><Type>ViewStudentReservations</Type></Request>"); // XML request
            String responseXML = serverConnection.readMessage();
            studentReservations = parseXMLResponse(responseXML);
        } catch (IOException e) {
            showErrorDialog("Error occurred: " + e.getMessage());
        }
        return studentReservations;
    }

    private List<StudentReservation> parseXMLResponse(String xmlResponse) {
        List<StudentReservation> reservations = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("Reservation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String reservationId = element.getElementsByTagName("reservation_id").item(0).getTextContent();
                String terminalId = element.getElementsByTagName("terminal_id").item(0).getTextContent();
                String terminalRoom = element.getElementsByTagName("terminal_room").item(0).getTextContent();
                String date = element.getElementsByTagName("date").item(0).getTextContent();
                String terminalStatus = element.getElementsByTagName("terminal_status").item(0).getTextContent();
                reservations.add(new StudentReservation(reservationId, terminalId, terminalRoom, date, terminalStatus));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
    public List<StudentReservation> loadStudentReservations() {
        return XMLUtility.loadStudentReservationsFromXML();
    }
}
