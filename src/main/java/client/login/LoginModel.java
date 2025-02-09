package client.login;

import java.io.*;
import java.net.Socket;

public class LoginModel {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public LoginModel() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read the welcome message from the server
            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String userID, String password, String userType) {
        try {
            // Send login request
            String loginRequest = String.format(
                    "<Login><UserID>%s</UserID><Password>%s</Password><UserType>%s</UserType></Login>",
                    userID, password, userType);
            writer.println(loginRequest);

            // Read server response
            String response = reader.readLine();
            System.out.println("Server Response: " + response);

            return "SUCCESS".equalsIgnoreCase(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendRequest(String message) {
        try {
            writer.println(message);
            String response = reader.readLine();
            System.out.println("Server Response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            writer.println("exit");
            socket.close();
            System.out.println("Logged out and socket closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
