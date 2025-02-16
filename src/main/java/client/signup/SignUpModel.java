package client.signup;

import client.utility.ServerConnectionManager;
import client.utility.ServerConnection;
import javax.swing.JOptionPane;
import javafx.application.Platform;
import java.io.IOException;

public class SignUpModel {
    private ServerConnection serverConnection;

    public SignUpModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public boolean register(String userID, String name, String password, String userType, String courseYear, String facultyType) {
        if (serverConnection == null) {
            showErrorDialog("Server is not available. Please try again later.");
            return false;
        }

        try {
            // Construct the XML request
            String signUpRequest = String.format(
                    "<SignUp><UserID>%s</UserID><Name>%s</Name><Password>%s</Password><UserType>%s</UserType><CourseYear>%s</CourseYear><FacultyType>%s</FacultyType></SignUp>",
                    userID, name, password, userType, courseYear, facultyType
            );

            // Send the request to the server
            serverConnection.sendMessage(signUpRequest);

            // Read the server's response
            String response = serverConnection.readMessage();

            // Debug: Print the response received
            System.out.println("DEBUG: Received response from server -> '" + response + "'");

            // Check for null response (possible connection issue)
            if (response == null) {
                System.out.println("DEBUG: Server response is null. Possible connection issue.");
                return false;
            }

            // Trim response and compare to "SUCCESS"
            return response.contains("<Status>SUCCESS</Status>");

        } catch (IOException e) {
            showErrorDialog("Lost connection to the server.");
            return false;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
