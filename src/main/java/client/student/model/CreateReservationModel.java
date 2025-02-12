package client.student.model;

import client.utility.ServerConnection;
import java.io.IOException;

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
}
