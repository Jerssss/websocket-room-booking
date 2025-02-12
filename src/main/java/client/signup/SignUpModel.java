package client.signup;

import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;
import javafx.application.Platform;

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
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public boolean register(String userID, String name, String password, String userType, String courseYear, String facultyType) {
        try {
            if (writer == null || reader == null) {
                showErrorDialog("Server is not available. Please try again later.");
                return false;
            }

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
            showErrorDialog("Lost connection to the server.");
            return false;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
