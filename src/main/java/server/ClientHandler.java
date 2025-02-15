package server;

import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private static final ConcurrentHashMap<String, Socket> activeSessions = new ConcurrentHashMap<>(); // Store active user sessions
    private final Socket clientSocket;
    private String loggedInUser = null; // Track logged-in user in this session

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("Welcome to the Server! Type 'exit' to logout.");

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from Client: " + clientMessage);

                if ("exit".equalsIgnoreCase(clientMessage)) {
                    logoutUser();
                    writer.println("Goodbye!");
                    break;
                }

                try {
                    if (clientMessage.contains("<Login>")) {
                        handleLogin(clientMessage, writer);
                    } else if (clientMessage.contains("<SignUp>")) {
                        handleSignUp(clientMessage, writer);
                    } else if (loggedInUser != null) {
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
            logoutUser(); // Ensure user is logged out if connection is lost
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        }
    }

    private void handleLogin(String clientMessage, PrintWriter writer) {
        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
        String password = extractField(clientMessage, "<Password>", "</Password>");
        String userType = extractField(clientMessage, "<UserType>", "</UserType>");

        if (userID == null || password == null || userType == null) {
            writer.println("<Response><Status>ERROR</Status><Message>Missing required fields for login.</Message></Response>");
            return;
        }

        synchronized (activeSessions) {
            if (activeSessions.containsKey(userID)) {
                writer.println("<Response><Status>ERROR</Status><Message>Account already logged in.</Message></Response>");
                return;
            }
        }

        boolean isValid = LoginProcessor.validateUser(userID, password, userType);
        if (isValid) {
            synchronized (activeSessions) {
                activeSessions.put(userID, clientSocket);
            }
            loggedInUser = userID;
            writer.println("<Response><Status>SUCCESS</Status><Message>Login Successful</Message></Response>");
            System.out.println("Login Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=SUCCESS");
        } else {
            writer.println("<Response><Status>FAILURE</Status><Message>Invalid credentials</Message></Response>");
            System.out.println("Login Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=FAILURE");
        }
    }

    private void handleSignUp(String clientMessage, PrintWriter writer) {
        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
        String name = extractField(clientMessage, "<Name>", "</Name>");
        String password = extractField(clientMessage, "<Password>", "</Password>");
        String userType = extractField(clientMessage, "<UserType>", "</UserType>");
        String courseYear = extractField(clientMessage, "<CourseYear>", "</CourseYear>");
        String facultyType = extractField(clientMessage, "<FacultyType>", "</FacultyType>");

        if (userID == null || name == null || password == null || userType == null) {
            writer.println("<Response><Status>ERROR</Status><Message>Missing required fields for sign-up.</Message></Response>");
            return;
        }

        boolean isRegistered = SignUpProcessor.registerUser(userID, name, password, userType, courseYear, facultyType);
        if (isRegistered) {
            writer.println("<Response><Status>SUCCESS</Status><Message>Registration Successful</Message></Response>");
            System.out.println("SignUp Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=SUCCESS");
        } else {
            writer.println("<Response><Status>FAILURE</Status><Message>Registration Failed</Message></Response>");
            System.out.println("SignUp Attempt: UserID=" + userID + ", UserType=" + userType + ", Result=FAILURE");
        }
    }

    private void logoutUser() {
        if (loggedInUser != null) {
            synchronized (activeSessions) {
                activeSessions.remove(loggedInUser);
            }
            System.out.println("User logged out: " + loggedInUser);
        }
    }

    private String extractField(String message, String startTag, String endTag) {
        try {
            if (message.contains(startTag) && message.contains(endTag)) {
                return message.split(startTag)[1].split(endTag)[0].trim();
            }
        } catch (Exception e) {
            System.out.println("Error extracting field: " + startTag);
        }
        return null;
    }
}
