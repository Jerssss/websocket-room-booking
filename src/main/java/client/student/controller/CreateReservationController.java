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
import java.time.LocalDate;
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
        System.out.println("[DEBUG] CreateReservationController initialized.");
    }

    private void closeWindow() {
        Stage stage = (Stage) view.getSaveChangesButton().getScene().getWindow();
        stage.close();
    }

    private void handleSaveChange(ActionEvent event) {
        System.out.println("[DEBUG] Entering handleSaveChange.");

        // Get session token and input values from view
        String sessionToken = getSessionToken();
        String reservationId = getNextReservationId();
        String userId = SessionManager.getUserId(sessionToken);

        // Process terminal ID:
        String terminalIdRaw = view.getTerminalNoTextField().getText().trim();
        String terminalId;
        if (terminalIdRaw.matches("\\d+")) {
            // If user typed just digits (e.g., "5"), prepend "PC"
            terminalId = "PC" + terminalIdRaw;
        } else if (terminalIdRaw.matches("(?i)^pc\\d+$")) {
            // If input already matches "PC" followed by digits (case-insensitive), use it as is
            terminalId = terminalIdRaw;
        } else {
            JOptionPane.showMessageDialog(null, "Error: Terminal ID format must match PC# (e.g. PC5).");
            return;
        }

        String room = view.getRoomNumberComboBox().getSelectionModel().getSelectedItem();
        LocalDate selectedDate = view.getDatePicker().getValue();
        String reservationDate = (selectedDate != null) ? selectedDate.toString() : "";
        String startTimeInput = view.getStartTimeTextField().getText().trim();
        String endTimeInput = view.getEndTimeTextField().getText().trim();
        String status = "Pending";

        System.out.println("[DEBUG] Inputs: terminalId=" + terminalId + ", room=" + room +
                ", reservationDate=" + reservationDate + ", startTime=" + startTimeInput +
                ", endTime=" + endTimeInput);

        // Validate session token
        if (sessionToken == null || sessionToken.isEmpty()) {
            System.out.println("[DEBUG] Invalid session token.");
            JOptionPane.showMessageDialog(null, "Session expired. Please log in again.");
            return;
        }

        // Validate that the terminal is active
        if (!isTerminalActive(terminalId)) {
            System.out.println("[DEBUG] Terminal " + terminalId + " is not active.");
            JOptionPane.showMessageDialog(null, "Error: Terminal is not active. Reservations can only be made to active terminals.");
            return;
        }

        // Validate that the terminal actually belongs to the selected room
        if (!doesTerminalMatchRoom(terminalId, room)) {
            System.out.println("[DEBUG] Terminal " + terminalId + " does not exist in room " + room);
            JOptionPane.showMessageDialog(null,
                    "Error: The terminal " + terminalId + " does not exist in room " + room + "!");
            return;
        }

        // Validate basic inputs
        if (terminalId.isEmpty() || room == null || selectedDate == null
                || startTimeInput.isEmpty() || endTimeInput.isEmpty()) {
            System.out.println("[DEBUG] One or more required fields are empty.");
            JOptionPane.showMessageDialog(null, "Error: All fields must be filled, including day and time.");
            return;
        }

        // Validate time format (military time XX:XX)
        if (!startTimeInput.matches("\\d{2}:\\d{2}") || !endTimeInput.matches("\\d{2}:\\d{2}")) {
            System.out.println("[DEBUG] Time format error: startTime=" + startTimeInput + ", endTime=" + endTimeInput);
            JOptionPane.showMessageDialog(null, "Error: Start time and end time must be in the format XX:XX (24-hour).");
            return;
        }

        // Validate that reservation date is at least 1 day in advance and within 1 month
        LocalDate today = LocalDate.now();
        if (!selectedDate.isAfter(today)) {
            System.out.println("[DEBUG] Reservation date " + selectedDate + " is not at least one day in advance.");
            JOptionPane.showMessageDialog(null, "Error: Reservations must be made at least 1 day in advance and cannot be in the past.");
            return;
        }
        if (selectedDate.isAfter(today.plusMonths(1))) {
            System.out.println("[DEBUG] Reservation date " + selectedDate + " exceeds the maximum allowed one month in advance.");
            JOptionPane.showMessageDialog(null, "Error: Reservations can be made at a maximum of 1 month in advance.");
            return;
        }

        // Parse user-entered times
        LocalTime start, end;
        try {
            start = LocalTime.parse(startTimeInput);
            end = LocalTime.parse(endTimeInput);
        } catch (DateTimeParseException e) {
            System.out.println("[DEBUG] Exception parsing times: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error: Invalid time format.");
            return;
        }

        // Validate time range (end must be after start, not exceeding 2 hours)
        if (end.isBefore(start)) {
            System.out.println("[DEBUG] End time " + endTimeInput + " is before start time " + startTimeInput + ".");
            JOptionPane.showMessageDialog(null, "Error: End time cannot be before start time.");
            return;
        }
        long durationMinutes = java.time.Duration.between(start, end).toMinutes();
        if (durationMinutes > 120) {
            System.out.println("[DEBUG] Reservation duration (" + durationMinutes + " minutes) exceeds max allowed 120 minutes.");
            JOptionPane.showMessageDialog(null, "Error: Reservation time cannot exceed 2 hours.");
            return;
        }

        // ===== NEW: Validate that user’s times are within the terminal’s operating hours =====
        Terminal terminalObj = getTerminalById(terminalId);
        if (terminalObj == null) {
            JOptionPane.showMessageDialog(null, "Error: Could not find terminal data for " + terminalId);
            return;
        }

        try {
            LocalTime terminalOpen = LocalTime.parse(terminalObj.getStartTime());
            LocalTime terminalClose = LocalTime.parse(terminalObj.getEndTime());
            // Check if user times are within operating hours
            if (start.isBefore(terminalOpen) || end.isAfter(terminalClose)) {
                JOptionPane.showMessageDialog(null,
                        "Error: Reservation must be within the terminal’s operating hours (" +
                                terminalOpen + " - " + terminalClose + ").");
                return;
            }
        } catch (DateTimeParseException e) {
            // If for some reason the terminal's start/end times aren't valid, log it
            System.out.println("[DEBUG] Terminal operating hours invalid: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error: Terminal's operating hours are invalid in XML. Contact admin.");
            return;
        }

        // Validate overlapping with existing reservations
        if (CreateReservationProcessor.isReservationOverlapping(terminalId, room, reservationDate, startTimeInput, endTimeInput)) {
            System.out.println("[DEBUG] Overlapping reservation found for terminal " + terminalId);
            JOptionPane.showMessageDialog(null, "Error: The selected terminal is already reserved for this time slot.");
            return;
        }

        // Process the reservation data
        System.out.println("[DEBUG] Calling CreateReservationProcessor.processReservationData with reservationId=" + reservationId);
        boolean success = CreateReservationProcessor.processReservationData(
                reservationId,
                userId,
                terminalId,
                room,
                reservationDate,
                startTimeInput,
                endTimeInput,
                status
        );

        if (success) {
            System.out.println("[DEBUG] Reservation processed successfully.");
            JOptionPane.showMessageDialog(null, "Success! Reservation has been added!");
        } else {
            System.out.println("[DEBUG] Reservation processing failed.");
            JOptionPane.showMessageDialog(null, "Error: Failed to create reservation. Try again.");
        }

        closeWindow();
        System.out.println("[DEBUG] Exiting handleSaveChange.");
    }

    // ========================= HELPER METHODS ========================= //

    /**
     * Returns the Terminal object for a given terminalId by parsing terminal.xml.
     */
    private Terminal getTerminalById(String terminalId) {
        List<Terminal> terminals = CreateReservationProcessor.parseXML("src/main/java/server/util/terminal.xml");
        for (Terminal t : terminals) {
            if (t.getTerminalId().equalsIgnoreCase(terminalId)) {
                return t;
            }
        }
        return null; // Not found
    }

    // Retrieve session token
    private String getSessionToken() {
        String token = SessionManager.getActiveSessionToken();
        System.out.println("[DEBUG] Retrieved session token: " + token);
        return token;
    }

    // Generate next reservation ID from XML
    private String getNextReservationId() {
        System.out.println("[DEBUG] Generating next reservation ID.");
        try {
            File xmlFile = new File("src/main/java/server/util/reservation_approval.xml");
            if (!xmlFile.exists()) {
                System.out.println("[DEBUG] Reservation XML file does not exist. Starting from ID 1.");
                return "1";
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList reservationList = doc.getElementsByTagName("Reservation");
            if (reservationList.getLength() == 0) {
                System.out.println("[DEBUG] No reservations found. Starting from ID 1.");
                return "1";
            }

            int maxId = 0;
            for (int i = 0; i < reservationList.getLength(); i++) {
                Node reservationNode = reservationList.item(i);
                if (reservationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element reservationElement = (Element) reservationNode;
                    Node idNode = reservationElement.getElementsByTagName("reservation_id").item(0);
                    if (idNode != null) {
                        String idStr = idNode.getTextContent().trim();
                        try {
                            int id = Integer.parseInt(idStr);
                            maxId = Math.max(maxId, id);
                        } catch (NumberFormatException e) {
                            System.err.println("[DEBUG] Invalid reservation ID found: " + idStr);
                        }
                    } else {
                        System.err.println("[DEBUG] Missing <reservation_id> for a reservation.");
                    }
                }
            }
            System.out.println("[DEBUG] Next reservation ID: " + (maxId + 1));
            return String.valueOf(maxId + 1);
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception in getNextReservationId: " + e.getMessage());
            e.printStackTrace();
            return "Error";
        }
    }

    /**
     * Checks if the given terminal ID actually belongs to the given room number
     * in the terminal.xml file.
     */
    private boolean doesTerminalMatchRoom(String terminalId, String room) {
        System.out.println("[DEBUG] Checking if terminal " + terminalId + " belongs to room " + room);
        List<Terminal> terminals = CreateReservationProcessor.parseXML("src/main/java/server/util/terminal.xml");
        for (Terminal terminal : terminals) {
            if (terminal.getTerminalId().equals(terminalId)) {
                // Compare the room in XML with the user-selected room
                if (terminal.getTerminalRoom().equals(room)) {
                    return true; // Found a matching terminal-room combo
                }
            }
        }
        return false; // No match found
    }

    // Check if the given terminal is active by parsing the terminal.xml file
    private boolean isTerminalActive(String terminalId) {
        System.out.println("[DEBUG] Checking if terminal " + terminalId + " is active.");
        List<Terminal> terminals = CreateReservationProcessor.parseXML("src/main/java/server/util/terminal.xml");
        for (Terminal terminal : terminals) {
            // Assuming Terminal has getTerminalId() and getTerminalStatus() methods.
            if (terminal.getTerminalId().equals(terminalId)) {
                System.out.println("[DEBUG] Terminal " + terminalId + " status: " + terminal.getTerminalStatus());
                return "Active".equalsIgnoreCase(terminal.getTerminalStatus());
            }
        }
        System.out.println("[DEBUG] Terminal " + terminalId + " not found or not active.");
        return false;
    }

    public static void redirectCreateReservationWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(CreateReservationController.class.getResource("/fxml/client/add_reservation_window.fxml"));
            Parent root = loader.load();
            CreateReservationView view = loader.getController();
            new CreateReservationController(view);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[DEBUG] Error loading Create Reservation GUI: " + e.getMessage());
        }
    }

    public static void loadDataFromXML(String filePath) {
        System.out.println("[DEBUG] Loading terminal data from XML: " + filePath);
        List<Terminal> reservation = CreateReservationProcessor.parseXML(filePath);
        if (reservation != null) {
            CreateReservationView.reservationData.clear();
            CreateReservationView.reservationData.addAll(reservation);
        }
    }

    public static void refreshTable() {
        String filePath = "src/main/java/server/util/terminal.xml";
        loadDataFromXML(filePath);
    }
}
