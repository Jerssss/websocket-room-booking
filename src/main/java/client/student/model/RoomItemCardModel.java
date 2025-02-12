package client.student.model;

import java.io.*;
import java.net.Socket;

public class RoomItemCardModel {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;


    public RoomItemCardModel() {
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
     * Fetches room details from the server.
     *
     * @param roomId The ID of the room.
     * @return A string containing room details (e.g., room type and available terminals).
     */
    public String fetchRoomDetails(String roomId) {
        try {
            // Send a request to fetch room details
            String request = String.format("<FetchRoomDetails><RoomID>%s</RoomID></FetchRoomDetails>", roomId);
            writer.println(request);

            // Read the server's response
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches terminal details for a room from the server.
     *
     * @param roomId The ID of the room.
     * @return A string containing terminal details.
     */
    public String fetchTerminalDetails(String roomId) {
        try {
            // Send a request to fetch terminal details
            String request = String.format("<FetchTerminalDetails><RoomID>%s</RoomID></FetchTerminalDetails>", roomId);
            writer.println(request);

            // Read the server's response
            return reader.readLine();
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