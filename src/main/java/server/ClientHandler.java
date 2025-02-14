package server;

import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;
import server.utility.LogsXMLHandler;

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
            String loggedInUserID = null;
            String loggedInUserType = null;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from Client: " + clientMessage);

                if ("exit".equalsIgnoreCase(clientMessage)) {
                    writer.println("Goodbye!");
                    if (isLoggedIn) LogsXMLHandler.logLogout(loggedInUserID, loggedInUserType);
                    break;
                }

                try {
                    if (clientMessage.contains("<Request>")) {
                        String requestType = extractField(clientMessage, "<Type>", "</Type>");

                        // Handle login request
                        if ("Login".equalsIgnoreCase(requestType)) {
                            String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                            String password = extractField(clientMessage, "<Password>", "</Password>");
                            String userType = extractField(clientMessage, "<UserType>", "</UserType>");

                            boolean isValid = LoginProcessor.validateUser(userID, password, userType);

                            String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                    + "<Response>"
                                    + "<Type>Login</Type>"
                                    + "<Status>" + (isValid ? "SUCCESS" : "FAILURE") + "</Status>"
                                    + "<UserID>" + userID + "</UserID>"
                                    + "</Response>";

                            writer.println(response);

                            if (isValid) {
                                LogsXMLHandler.saveLog(userID, "Login Success", userType);
                                isLoggedIn = true;
                                loggedInUserID = userID;
                                loggedInUserType = userType;
                            } else {
                                LogsXMLHandler.saveLog(userID, "Login Failure", userType);
                            }

                            System.out.println("Login Attempt: UserID=" + userID + ", Result=" + (isValid ? "SUCCESS" : "FAILURE"));
                        }

                        // Handle sign-up request
                        else if ("SignUp".equalsIgnoreCase(requestType)) {
                            String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                            String name = extractField(clientMessage, "<Name>", "</Name>");
                            String password = extractField(clientMessage, "<Password>", "</Password>");
                            String userType = extractField(clientMessage, "<UserType>", "</UserType>");
                            String courseYear = extractField(clientMessage, "<CourseYear>", "</CourseYear>");
                            String facultyType = extractField(clientMessage, "<FacultyType>", "</FacultyType>");

                            boolean isRegistered = SignUpProcessor.registerUser(userID, name, password, userType, courseYear, facultyType);

                            String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                    + "<Response>"
                                    + "<Type>SignUp</Type>"
                                    + "<Status>" + (isRegistered ? "SUCCESS" : "FAILURE") + "</Status>"
                                    + "<UserID>" + userID + "</UserID>"
                                    + "</Response>";

                            writer.println(response);

                            if (isRegistered) {
                                LogsXMLHandler.saveLog(userID, "SignUp Success", userType);
                            } else {
                                LogsXMLHandler.saveLog(userID, "SignUp Failure", userType);
                            }

                            System.out.println("SignUp Attempt: UserID=" + userID + ", Result=" + (isRegistered ? "SUCCESS" : "FAILURE"));
                        }

                        // Handle other operations after login
                        else if (isLoggedIn) {
                            writer.println("Request Received: " + clientMessage);
                            System.out.println("Processing client request: " + clientMessage);
                        } else {
                            writer.println("Please log in first.");
                        }
                    } else {
                        writer.println("Invalid request format.");
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
