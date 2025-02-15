package client.login;

import client.utility.ServerConnectionManager;
import client.utility.ServerConnection;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LoginModel {
    private ServerConnection serverConnection;

    public LoginModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
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
            String loginRequest = String.format(
                    "<Login><UserID>%s</UserID><Password>%s</Password><UserType>%s</UserType></Login>",
                    userID, password, userType
            );
            serverConnection.sendMessage(loginRequest);
            String response = serverConnection.readMessage();
            return parseXMLResponse(response);
        } catch (IOException e) {
            showErrorDialog("Lost connection to the server.");
            return false;
        }
    }

    private boolean parseXMLResponse(String responseXML) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(responseXML.getBytes()));
            Element root = doc.getDocumentElement();
            String status = root.getElementsByTagName("Status").item(0).getTextContent();
            return status.equalsIgnoreCase("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
