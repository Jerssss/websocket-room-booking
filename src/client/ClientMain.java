package client;

import java.io.*;
import java.net.Socket;

public class ClientMain {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             PrintWriter writer = new PrintWriter(output, true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");

            // Read server welcome message
            System.out.println(reader.readLine());

            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);
                String serverResponse = reader.readLine();
                System.out.println(serverResponse);

                if ("Goodbye!".equalsIgnoreCase(serverResponse)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
        }
    }
}
