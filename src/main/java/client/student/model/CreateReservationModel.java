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
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createReservation(String startTime, String endTime, String date, String roomId) {
        if (serverConnection == null) return false;

        try {
            String reservationRequest = String.format(
                    "<Reservation><StartTime>%s</StartTime><EndTime>%s</EndTime><Date>%s</Date><RoomID>%s</RoomID></Reservation>",
                    startTime, endTime, date, roomId
            );

            serverConnection.sendMessage(reservationRequest);
            String response = serverConnection.readMessage();
            System.out.println("Server Response: " + response);

            return "SUCCESS".equalsIgnoreCase(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String fetchAvailableRooms(String startTime, String endTime, String date) {
        if (serverConnection == null) return null;

        try {
            String fetchRoomsRequest = String.format(
                    "<FetchRooms><StartTime>%s</StartTime><EndTime>%s</EndTime><Date>%s</Date></FetchRooms>",
                    startTime, endTime, date
            );

            serverConnection.sendMessage(fetchRoomsRequest);
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

                    if ("Available".equalsIgnoreCase(status)) {
                        roomData.putIfAbsent(room, new HashMap<>());
                        Map<String, Integer> osCounts = roomData.get(room);
                        osCounts.put(os, osCounts.getOrDefault(os, 0) + 1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomData;
    }
}
