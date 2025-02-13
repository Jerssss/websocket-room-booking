package client.student.model;

import client.utility.ServerConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateReservationModel {
    private ServerConnection serverConnection;

    public CreateReservationModel() {
        try {
            // Establish a connection to the server
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a reservation request to the server.
     *
     * @param startTime The start time of the reservation in HH:MM format.
     * @param endTime   The end time of the reservation in HH:MM format.
     * @param date      The date of the reservation in MM/DD/YY format.
     * @param roomId    The ID of the room to be reserved.
     * @return true if the reservation was successful, false otherwise.
     */
    public boolean createReservation(String startTime, String endTime, String date, String roomId) {
        if (serverConnection == null) return false;

        try {
            // Format the reservation request
            String reservationRequest = String.format(
                    "<Reservation><StartTime>%s</StartTime><EndTime>%s</EndTime><Date>%s</Date><RoomID>%s</RoomID></Reservation>",
                    startTime, endTime, date, roomId
            );

            // Send the reservation request to the server
            serverConnection.sendMessage(reservationRequest);

            // Read the server's response
            String response = serverConnection.readMessage();
            System.out.println("Server Response: " + response);

            // Return true if the reservation was successful
            return "SUCCESS".equalsIgnoreCase(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a request to the server to fetch available rooms based on the provided time and date.
     *
     * @param startTime The start time of the reservation in HH:MM format.
     * @param endTime   The end time of the reservation in HH:MM format.
     * @param date      The date of the reservation in MM/DD/YY format.
     * @return A string containing the list of available rooms, or null if an error occurs.
     */
    public String fetchAvailableRooms(String startTime, String endTime, String date) {
        if (serverConnection == null) return null;

        try {
            // Format the request to fetch available rooms
            String fetchRoomsRequest = String.format(
                    "<FetchRooms><StartTime>%s</StartTime><EndTime>%s</EndTime><Date>%s</Date></FetchRooms>",
                    startTime, endTime, date
            );

            // Send the request to the server
            serverConnection.sendMessage(fetchRoomsRequest);

            // Read the server's response
            return serverConnection.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Map<String, Map<String, Integer>> parseTerminals(String filePath) {
        Map<String, Map<String, Integer>> roomData = new HashMap<>();

        try {
            File file = new File(filePath);
            System.out.println("Loading XML from: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.out.println("ERROR: XML file not found!");
                return roomData;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Terminal");

            System.out.println("Total Terminals Found: " + nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String room = element.getElementsByTagName("terminal_room").item(0).getTextContent().trim();
                    String os = element.getElementsByTagName("terminal_os").item(0).getTextContent().trim();
                    String status = element.getElementsByTagName("terminal_status").item(0).getTextContent().trim();

                    System.out.println("Processing: Room=" + room + ", OS=" + os + ", Status=" + status);

                    if ("Available".equalsIgnoreCase(status)) {
                        roomData.putIfAbsent(room, new HashMap<>());
                        Map<String, Integer> osCounts = roomData.get(room);

                        // Ensure different OS counts separately
                        osCounts.put(os, osCounts.getOrDefault(os, 0) + 1);
                    }
                }
            }

            // Debug Output
            System.out.println("Final Room Data:");
            for (Map.Entry<String, Map<String, Integer>> roomEntry : roomData.entrySet()) {
                System.out.println("Room: " + roomEntry.getKey());
                for (Map.Entry<String, Integer> osEntry : roomEntry.getValue().entrySet()) {
                    System.out.println("  OS: " + osEntry.getKey() + ", Available: " + osEntry.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomData;
    }


}
