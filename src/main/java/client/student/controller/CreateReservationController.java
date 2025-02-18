package client.student.controller;

import client.student.model.CreateReservationModel;
import client.student.view.CreateReservationView;

import client.utility.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.student.CreateReservationProcessor;
import server.utility.Terminal;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CreateReservationController {

    private final CreateReservationView view;
    private final CreateReservationModel model;

    public CreateReservationController(CreateReservationView view) {
        this.view = view;
        this.model = new CreateReservationModel();
        this.view.setSaveChangesButtonAction(this::handleSaveChange);
    }

    private void closeWindow() {
        Stage stage = (Stage) view.getSaveChangesButton().getScene().getWindow();
        stage.close();
    }

    private void handleSaveChange(ActionEvent event) {
        // Get selected values from combo boxes
        String sessionToken = getSessionToken(); // Get the session token

        String reservationId = getNextReservationId();
        String userId = SessionManager.getUserId(sessionToken);
        String terminalId = view.getTerminalNoTextField().getText().trim();
        String room = view.getRoomNumberComboBox().getSelectionModel().getSelectedItem();
        String reservationDate = view.getDatePicker().getValue().toString();
        String startTime = view.getStartTimeTextField().getText().trim();
        String endTime = view.getEndTimeTextField().getText().trim();
        String status = "Pending";

        // Validate session token
        if (sessionToken == null || sessionToken.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Session expired. Please log in again.");
            return; // Prevent further actions if session is invalid
        }

        // Validate inputs
        if (terminalId.isEmpty() || room == null || reservationDate == null) {
            JOptionPane.showMessageDialog(null, "Error: All fields must be filled, including day and time.");
            closeWindow(); // Close window even if there's an error
            return;
        }

        // Validate Terminal ID - must be numeric
        if (!terminalId.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Error: Terminal ID must be a number.");
            closeWindow(); // Close window even if there's an error
            return;
        }

        // Validate Start Time and End Time format (XX:XX military time)
        if (!startTime.matches("\\d{2}:\\d{2}") || !endTime.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Error: Start time and end time must be in the format XX:XX (military time).");
            closeWindow();
            return;
        }

        // Validate that the start and end times are within a valid range
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            // Check that end time is after start time
            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(null, "Error: End time cannot be before start time.");
                closeWindow();
                return;
            }

            // Calculate the duration of the reservation
            long duration = java.time.Duration.between(start, end).toHours();

            // Validate if duration exceeds 2 hours
            if (duration > 2) {
                JOptionPane.showMessageDialog(null, "Error: Reservation time cannot exceed 2 hours.");
                closeWindow();
                return;
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid time format.");
            closeWindow();
            return;
        }

        // Check if the user already has a pending reservation
        if (hasPendingReservation(userId)) {
            JOptionPane.showMessageDialog(null, "Error: You already have a pending reservation. Please finalize it first.");
            closeWindow();
            return;
        }

        // Get the next reservation ID by parsing the reservation_approval.xml
        view.setTerminalId(terminalId);
        view.setRoom(room);

        // Process the reservation data with the reservation details
        boolean success = CreateReservationProcessor.processReservationData(
                reservationId,
                userId,
                terminalId,
                room,
                reservationDate,
                startTime,
                endTime,
                "Pending" // Assuming the initial status is "Pending"
        );

        // Handle the result of reservation processing
        if (success) {
            JOptionPane.showMessageDialog(null, "Success! Reservation has been added!");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Failed to create reservation. Try again.");
        }

        closeWindow(); // Always close the window at the end
    }

    // Method to check if the user has a pending reservation
    private boolean hasPendingReservation(String userId) {
        // Assuming you have a method to fetch reservations from the XML or database
        try {
            File xmlFile = new File("server/util/reservation_approval.xml");

            if (!xmlFile.exists()) {
                return false; // If the XML file doesn't exist, assume no reservations
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList reservationList = doc.getElementsByTagName("Reservation");

            for (int i = 0; i < reservationList.getLength(); i++) {
                Node reservationNode = reservationList.item(i);
                if (reservationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element reservationElement = (Element) reservationNode;

                    String currentUserId = reservationElement.getElementsByTagName("user_id").item(0).getTextContent().trim();
                    String status = reservationElement.getElementsByTagName("status").item(0).getTextContent().trim();

                    // Check if the user has a pending reservation
                    if (currentUserId.equals(userId) && "Pending".equals(status)) {
                        return true; // User has a pending reservation
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // No pending reservations for the user
    }

    private String getSessionToken() {
        return SessionManager.getActiveSessionToken();
    }

    private String getNextReservationId() {
        try {
            File xmlFile = new File("server/util/reservation_approval.xml");

            if (!xmlFile.exists()) {
                System.out.println("XML file does not exist. Starting from ID 1.");
                return "1"; // Start from 1 if the file does not exist
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList reservationList = doc.getElementsByTagName("Reservation");

            if (reservationList.getLength() == 0) {
                System.out.println("No reservations found. Starting from ID 1.");
                return "1"; // No reservations yet
            }

            int maxId = 0;
            System.out.println("Existing Reservation IDs:");

            for (int i = 0; i < reservationList.getLength(); i++) {
                Node reservationNode = reservationList.item(i);
                if (reservationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element reservationElement = (Element) reservationNode;
                    Node idNode = reservationElement.getElementsByTagName("reservation_id").item(0);

                    if (idNode != null) {
                        String idStr = idNode.getTextContent().trim();
                        try {
                            int id = Integer.parseInt(idStr);
                            System.out.println("Found ID: " + id);
                            maxId = Math.max(maxId, id); // Track the highest reservation ID
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid reservation ID found: " + idStr);
                        }
                    } else {
                        System.err.println("Missing <reservation_id> for a reservation.");
                    }
                }
            }

            // Return the next reservation ID by incrementing the highest found ID
            System.out.println("Next reservation ID: " + (maxId + 1));
            return String.valueOf(maxId + 1);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error"; // Ensure a string is always returned
        }
    }





    public static void redirectCreateReservationWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(CreateReservationController.class.getResource("/fxml/client/add_reservation_window.fxml"));
            Parent root = loader.load();
            CreateReservationView view = loader.getController();
            CreateReservationController controller = new CreateReservationController(view);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Create Reservation GUI: " + e.getMessage());
        }
    }

    public static void loadDataFromXML(String filePath) {
        List<Terminal> reservation = CreateReservationProcessor.parseXML(filePath);
        if (reservation != null) {
            CreateReservationView.reservationData.clear(); // Clear the current data
            CreateReservationView.reservationData.addAll(reservation);
        }
    }

    public static void refreshTable() {
        String filePath = "src/main/java/server/util/terminal.xml";
        loadDataFromXML(filePath);
    }
}

