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
            // Construct XML sign-up request
            String signUpRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<Request>"
                    + "<Type>SignUp</Type>"
                    + "<UserID>" + userID + "</UserID>"
                    + "<Name>" + name + "</Name>"
                    + "<Password>" + password + "</Password>"
                    + "<UserType>" + userType + "</UserType>"
                    + "<CourseYear>" + courseYear + "</CourseYear>"
                    + "<FacultyType>" + facultyType + "</FacultyType>"
                    + "</Request>";

            serverConnection.sendMessage(signUpRequest);

            // Receive XML response
            String response = serverConnection.readMessage();
            System.out.println("Server Response: " + response);

            // Check for success in the response
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
