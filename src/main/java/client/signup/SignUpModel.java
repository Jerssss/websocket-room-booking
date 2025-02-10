package client.signup;

import java.io.*;
import java.net.Socket;

public class SignUpModel {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public SignUpModel() {
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

    public boolean register(String userID, String name, String password, String userType, String courseYear, String facultyType) {
        try {
            // Send sign-up request
            String signUpRequest = String.format(
                    "<SignUp><UserID>%s</UserID><Name>%s</Name><Password>%s</Password><UserType>%s</UserType><CourseYear>%s</CourseYear><FacultyType>%s</FacultyType></SignUp>",
                    userID, name, password, userType, courseYear, facultyType);
            writer.println(signUpRequest);

            // Read server response
            String response = reader.readLine();
            System.out.println("Server Response: " + response);

            return "SUCCESS".equalsIgnoreCase(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
