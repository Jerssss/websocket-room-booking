package client.signup;

import client.utility.ServerConnection;
import javax.swing.JOptionPane;
import javafx.application.Platform;
import java.io.IOException;

public class SignUpModel {
    private ServerConnection serverConnection;

    public SignUpModel() {
        try {
            serverConnection = new ServerConnection();
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
            String signUpRequest = String.format(
                    "<SignUp><UserID>%s</UserID><Name>%s</Name><Password>%s</Password><UserType>%s</UserType><CourseYear>%s</CourseYear><FacultyType>%s</FacultyType></SignUp>",
                    userID, name, password, userType, courseYear, facultyType
            );
            serverConnection.sendMessage(signUpRequest);

            String response = serverConnection.readMessage();
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
