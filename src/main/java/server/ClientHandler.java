package server;

import server.landingpage.LoginProcessor;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("Welcome to the Server! Type 'exit' to logout.");

            String clientMessage;
            boolean isLoggedIn = false;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from Client: " + clientMessage); // Log client request

                if ("exit".equalsIgnoreCase(clientMessage)) {
                    writer.println("Goodbye!");
                    break;
                }

                if (clientMessage.contains("<Login>")) {
                    // Handle login request
                    String userID = clientMessage.split("<UserID>")[1].split("</UserID>")[0];
                    String password = clientMessage.split("<Password>")[1].split("</Password>")[0];
                    String userType = clientMessage.split("<UserType>")[1].split("</UserType>")[0];

                    boolean isValid = LoginProcessor.validateUser(userID, password, userType);
                    if (isValid) {
                        writer.println("SUCCESS");
                        isLoggedIn = true; // Mark the client as logged in
                        System.out.println("Login Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=SUCCESS");
                    } else {
                        writer.println("FAILURE");
                        System.out.println("Login Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=FAILURE");
                    }
                } else if (isLoggedIn) {
                    // Handle other client requests (e.g., CRUD operations)
                    writer.println("Request Received: " + clientMessage);
                    System.out.println("Processing client request: " + clientMessage);
                } else {
                    writer.println("Please log in first.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
