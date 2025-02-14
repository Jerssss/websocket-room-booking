package client.login;

import client.utility.ServerConnection;
import javax.swing.JOptionPane;
import javafx.application.Platform;
import java.io.IOException;

public class LoginModel {
    private ServerConnection serverConnection;

    public LoginModel() {
        try {
            serverConnection = new ServerConnection();
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    public boolean authenticate(String userID, String password, String userType) {
        if (serverConnection == null) {
            showErrorDialog("Server is not available. Please try again later.");
            return false;
        }

        try {
            // Construct XML request
            String loginRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<Request>"
                    + "<Type>Login</Type>"
                    + "<UserID>" + userID + "</UserID>"
                    + "<Password>" + password + "</Password>"
                    + "<UserType>" + userType + "</UserType>"
                    + "</Request>";

            serverConnection.sendMessage(loginRequest);

            // Receive XML response
            String responseXML = serverConnection.readMessage();
            System.out.println("Server Response: " + responseXML);

            return responseXML.contains("<Status>SUCCESS</Status>");

        } catch (IOException e) {
            showErrorDialog("Lost connection to the server.");
            return false;
        }
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
