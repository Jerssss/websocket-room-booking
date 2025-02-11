package client.student.model;

import java.io.*;
import java.net.Socket;

public class CreateReservationModel {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public CreateReservationModel() {
        try {
            // Establish a connection to the server
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read the welcome message from the server
            System.out.println(reader.readLine());
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
        try {
            // Format the reservation request
            String reservationRequest = String.format(
                    "<Reservation><StartTime>%s</StartTime><EndTime>%s</EndTime><Date>%s</Date><RoomID>%s</RoomID></Reservation>",
                    startTime, endTime, date, roomId);

            // Send the reservation request to the server
            writer.println(reservationRequest);

            // Read the server's response
            String response = reader.readLine();
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
        try {
            // Format the request to fetch available rooms
            String fetchRoomsRequest = String.format(
                    "<FetchRooms><StartTime>%s</StartTime><EndTime>%s</EndTime><Date>%s</Date></FetchRooms>",
                    startTime, endTime, date);

            // Send the request to the server
            writer.println(fetchRoomsRequest);

            // Read the server's response
            String response = reader.readLine();
            System.out.println("Server Response: " + response);

            // Return the list of available rooms
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the connection to the server.
     */
    public void closeConnection() {
        try {
            if (writer != null) {
                writer.println("exit");
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}