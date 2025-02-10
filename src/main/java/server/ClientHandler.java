package server;

import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;

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

                try {
                    if (clientMessage.contains("<Login>")) {
                        // Handle login request
                        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                        String password = extractField(clientMessage, "<Password>", "</Password>");
                        String userType = extractField(clientMessage, "<UserType>", "</UserType>");

                        if (userID == null || password == null || userType == null) {
                            writer.println("ERROR: Missing required fields for login.");
                            continue;
                        }

                        boolean isValid = LoginProcessor.validateUser(userID, password, userType);
                        if (isValid) {
                            writer.println("SUCCESS");
                            isLoggedIn = true; // Mark the client as logged in
                            System.out.println("Login Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=SUCCESS");
                        } else {
                            writer.println("FAILURE");
                            System.out.println("Login Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=FAILURE");
                        }
                    } else if (clientMessage.contains("<SignUp>")) {
                        // Handle sign-up request
                        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                        String name = extractField(clientMessage, "<Name>", "</Name>");
                        String password = extractField(clientMessage, "<Password>", "</Password>");
                        String userType = extractField(clientMessage, "<UserType>", "</UserType>");
                        String courseYear = extractField(clientMessage, "<CourseYear>", "</CourseYear>");
                        String facultyType = extractField(clientMessage, "<FacultyType>", "</FacultyType>");

                        if (userID == null || name == null || password == null || userType == null) {
                            writer.println("ERROR: Missing required fields for sign-up.");
                            continue;
                        }

                        boolean isRegistered = SignUpProcessor.registerUser(userID, name, password, userType, courseYear, facultyType);
                        if (isRegistered) {
                            writer.println("SUCCESS");
                            System.out.println("SignUp Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=SUCCESS");
                        } else {
                            writer.println("FAILURE");
                            System.out.println("SignUp Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=FAILURE");
                        }
                    } else if (isLoggedIn) {
                        // Handle other client requests (e.g., CRUD operations)
                        writer.println("Request Received: " + clientMessage);
                        System.out.println("Processing client request: " + clientMessage);
                    } else {
                        writer.println("Please log in first.");
                    }
                } catch (Exception e) {
                    writer.println("ERROR: Malformed request.");
                    System.out.println("Error processing client message: " + e.getMessage());
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

    // Helper method to extract fields from XML-like messages
    private String extractField(String message, String startTag, String endTag) {
        try {
            if (message.contains(startTag) && message.contains(endTag)) {
                return message.split(startTag)[1].split(endTag)[0].trim();
            }
        } catch (Exception e) {
            System.out.println("Error extracting field: " + startTag);
        }
        return null; // Return null if the field is missing or malformed
    }
}
